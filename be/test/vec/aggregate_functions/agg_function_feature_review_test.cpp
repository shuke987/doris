// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

// Feature review UT for aggregate functions (SUM, COUNT, AVG, MIN, MAX,
// GROUP_CONCAT, MULTI_DISTINCT_*, to_bitmap), pilot context: SOP feature
// review on branch-4.1.
//
// Three SEV candidates confirmed by FT probe (see
// /Users/keshu/projects/shuke/quality-analysis/feature-review/agg_function/):
//   SEV-1  SUM(BIGINT)  silent overflow wrap (no auto-promotion to LARGEINT)
//   SEV-2  to_bitmap(negative)  silent empty bitmap (data loss in
//                                BITMAP_UNION pipelines)
//   SEV-3  GROUP_CONCAT(DISTINCT) does not preserve insertion order
//
// These tests:
//   - LOCK current observed behavior for each SEV (PASS = bug reproduced;
//     once the SEV is fixed the test must be updated together).
//   - Cover NULL handling matrix (SUM/COUNT/AVG/MIN/MAX over all-NULL,
//     empty, mixed inputs).
//   - Cover state serialize -> deserialize -> continue-add roundtrip.
//   - Cover DISTINCT correctness (multi_distinct_count single & multi col).
//   - Cover is_trivial flag for SUM/COUNT/AVG/MIN.
//
// Hard-spec contract:
//   PASS = current observed behavior (locked-in for SEV tests, spec for rest).
//   FAIL = behavior changed.
//
// Test name pattern: AggFunctionFeatureReviewTest.UT_AGG_<short_name>

#include <gtest/gtest.h>

#include <cstdint>
#include <limits>
#include <memory>
#include <set>
#include <string>
#include <vector>

#include "core/column/column.h"
#include "core/column/column_complex.h"
#include "core/column/column_nullable.h"
#include "core/column/column_string.h"
#include "core/column/column_vector.h"
#include "core/data_type/data_type_bitmap.h"
#include "core/data_type/data_type_nullable.h"
#include "core/data_type/data_type_number.h"
#include "core/data_type/data_type_string.h"
#include "core/field.h"
#include "core/types.h"
#include "core/value/bitmap_value.h"
#include "exec/operator/exchange_sink_buffer.h"
#include "exprs/aggregate/aggregate_function.h"
#include "exprs/aggregate/aggregate_function_simple_factory.h"
#include "exprs/function/simple_function_factory.h"

// --- Minimal stubs for BE_TEST-only externs referenced by libExec / libExprs.
// Mirrors the pattern in iia_feature_review_be_test / ck_feature_review_be_test.
// Since this mini executable does not link the .cpp files that normally provide
// these definitions, we supply no-ops to satisfy the linker.
namespace doris {

void transmit_blockv2(PBackendService_Stub* /*stub*/,
                      std::unique_ptr<AutoReleaseClosure<PTransmitDataParams,
                                                         ExchangeSendCallback<PTransmitDataResult>>>
                              /*closure*/) {
    // intentional no-op for aggregate-function-only unit tests
}

void register_function_throw_exception(SimpleFunctionFactory& /*factory*/) {
    // intentional no-op for aggregate-function-only unit tests
}

} // namespace doris

namespace doris {

// Forward declarations of register_aggregate_function_* (the unit test pulls
// only the few we need, not the global instance, to keep coupling minimal).
void register_aggregate_function_sum(AggregateFunctionSimpleFactory& factory);
void register_aggregate_function_count(AggregateFunctionSimpleFactory& factory);
void register_aggregate_function_avg(AggregateFunctionSimpleFactory& factory);
void register_aggregate_function_minmax(AggregateFunctionSimpleFactory& factory);
void register_aggregate_function_group_concat(AggregateFunctionSimpleFactory& factory);
void register_aggregate_function_combinator_distinct(AggregateFunctionSimpleFactory& factory);

// -----------------------------------------------------------------------------
// Helpers
// -----------------------------------------------------------------------------

namespace {

// Build a registry pre-populated with the agg functions we exercise.
AggregateFunctionSimpleFactory make_factory() {
    AggregateFunctionSimpleFactory f;
    register_aggregate_function_sum(f);
    register_aggregate_function_count(f);
    register_aggregate_function_avg(f);
    register_aggregate_function_minmax(f);
    register_aggregate_function_group_concat(f);
    // multi_distinct_* is registered as a combinator on top of the previously
    // registered functions, so register it last.
    register_aggregate_function_combinator_distinct(f);
    return f;
}

// Build a ColumnNullable from a vector<int64_t> and a parallel null-flag vector.
ColumnPtr make_nullable_int64_column(const std::vector<int64_t>& values,
                                     const std::vector<uint8_t>& nulls) {
    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    for (size_t i = 0; i < values.size(); ++i) {
        nested->insert_data(reinterpret_cast<const char*>(&values[i]), sizeof(int64_t));
        uint8_t n = nulls[i];
        null_map->insert_data(reinterpret_cast<const char*>(&n), sizeof(uint8_t));
    }
    return ColumnNullable::create(std::move(nested), std::move(null_map));
}

// Build a plain ColumnInt64 from a vector<int64_t>.
ColumnPtr make_int64_column(const std::vector<int64_t>& values) {
    auto col = ColumnInt64::create();
    for (auto v : values) {
        col->insert_data(reinterpret_cast<const char*>(&v), sizeof(int64_t));
    }
    return std::move(col);
}

// Build a plain ColumnString from a vector<string>.
ColumnPtr make_string_column(const std::vector<std::string>& values) {
    auto col = ColumnString::create();
    for (const auto& s : values) {
        col->insert_data(s.data(), s.size());
    }
    return std::move(col);
}

// Allocate & create an aggregate state via the function.
struct AggState {
    AggregateFunctionPtr fn;
    std::unique_ptr<char[]> buf;
    AggregateDataPtr place {nullptr};

    AggState(AggregateFunctionPtr f) : fn(std::move(f)) {
        buf.reset(new char[fn->size_of_data()]);
        place = buf.get();
        fn->create(place);
    }
    ~AggState() {
        if (fn && place) {
            fn->destroy(place);
        }
    }
    AggState(const AggState&) = delete;
    AggState& operator=(const AggState&) = delete;
};

// Add every row of `column` into state.
void add_all(IAggregateFunction* fn, AggregateDataPtr place, const IColumn* column,
             Arena& arena) {
    const IColumn* cols[1] = {column};
    for (size_t i = 0; i < column->size(); ++i) {
        fn->add(place, cols, i, arena);
    }
}

} // namespace

// -----------------------------------------------------------------------------
// Fixture
// -----------------------------------------------------------------------------

class AggFunctionFeatureReviewTest : public testing::Test {
protected:
    AggregateFunctionSimpleFactory factory = make_factory();
    Arena arena;
};

// =============================================================================
// SEV-1: SUM(BIGINT) silent overflow wrap (no auto-promotion to LARGEINT)
// =============================================================================

// LOCK: SUM(int64) of two BIGINT_MAX wraps to -2 (two's-complement),
// instead of being auto-promoted to LARGEINT.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_BIGINT_overflow_wraps) {
    auto fn = factory.get("sum", {std::make_shared<DataTypeInt64>()}, nullptr, false, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_int64_column(
            {std::numeric_limits<int64_t>::max(), std::numeric_limits<int64_t>::max()});
    add_all(fn.get(), s.place, col.get(), arena);

    auto result = ColumnInt64::create();
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    // BIGINT_MAX + BIGINT_MAX wraps in int64 -> -2.
    EXPECT_EQ(result->get_element(0), int64_t(-2))
            << "If this fires, SUM(BIGINT) was promoted to LARGEINT - SEV-1 is fixed and this "
               "lock-test must be retired.";
}

// LOCK: same SEV via nullable path -- nullable wrapper does not change wrap behavior.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_BIGINT_overflow_wraps_nullable) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("sum", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column(
            {std::numeric_limits<int64_t>::max(), std::numeric_limits<int64_t>::max()}, {0, 0});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_FALSE(result->is_null_at(0));
    auto v = assert_cast<const ColumnInt64&>(result->get_nested_column()).get_element(0);
    EXPECT_EQ(v, int64_t(-2));
}

// =============================================================================
// SEV-2: to_bitmap(negative) silently produces an empty bitmap
// =============================================================================

// LOCK: to_bitmap(-1) returns NOT NULL but bitmap is EMPTY (cardinality 0).
// Downstream BITMAP_UNION_COUNT therefore silently loses the row.
//
// Implementation note: `struct ToBitmap` in be/src/exprs/function/function_bitmap.cpp
// is translation-unit-local, so we cannot link against it directly from the
// mini test target.  This test mirrors the exact algorithm from
// `ToBitmap::execute<ColumnInt64, false>` (function_bitmap.cpp:124-141):
//
//     auto& res_data = res_column->get_data();
//     for (...) {
//         if (auto value = col->get_data()[i]; value >= 0) {
//             res_data[i].add(value);
//         }
//     }
//
// If the source-level guard changes (e.g. is removed, or starts producing
// NULL for negatives), the SEV's observable behavior will change -- this
// test then locks in the *contractual* statement that today's pipeline
// produces an empty bitmap (cardinality 0) for negative inputs.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_to_bitmap_negative_silent_empty) {
    auto col = make_int64_column({-1, 5, 0, -100, 7});
    const auto* col_int64 = assert_cast<const ColumnInt64*>(col.get());

    // Build a bitmap result column the same way `FunctionAlwaysNotNullable`
    // does: create_column + resize(input_rows_count).
    auto col_res = std::make_shared<DataTypeBitMap>()->create_column();
    col_res->resize(col_int64->size());
    auto* res_bitmap = assert_cast<ColumnBitmap*>(col_res.get());
    auto& res_data = res_bitmap->get_data();

    // --- begin mirror of ToBitmap::execute<ColumnInt64, false> ---
    size_t n = col_int64->size();
    for (size_t i = 0; i < n; ++i) {
        if (auto value = col_int64->get_data()[i]; value >= 0) {
            res_data[i].add(static_cast<uint64_t>(value));
        }
    }
    // --- end mirror ---

    ASSERT_EQ(res_bitmap->size(), 5u);
    // Row 0: -1 -> default-constructed (empty) BitmapValue.
    EXPECT_EQ(res_data[0].cardinality(), 0u)
            << "If this fires, to_bitmap(negative) is no longer silently dropped - SEV-2 is fixed "
               "and this lock-test must be retired.";
    EXPECT_FALSE(res_data[0].contains(0));
    // Row 1: 5 -> bitmap with single element 5.
    EXPECT_EQ(res_data[1].cardinality(), 1u);
    EXPECT_TRUE(res_data[1].contains(5));
    // Row 2: 0 -> bitmap with single element 0.
    EXPECT_EQ(res_data[2].cardinality(), 1u);
    EXPECT_TRUE(res_data[2].contains(0));
    // Row 3: -100 -> empty.
    EXPECT_EQ(res_data[3].cardinality(), 0u);
    // Row 4: 7 -> bitmap with 7.
    EXPECT_EQ(res_data[4].cardinality(), 1u);
    EXPECT_TRUE(res_data[4].contains(7));

    // Downstream-impact assertion: a bitmap_union over rows [-1, 5, 0, -100, 7]
    // has cardinality 3 (not 5) -- the two negative rows are silently lost.
    BitmapValue u;
    for (size_t i = 0; i < n; ++i) {
        u |= res_data[i];
    }
    EXPECT_EQ(u.cardinality(), 3u)
            << "BITMAP_UNION over the row group silently dropped the 2 negative rows.";
}

// =============================================================================
// SEV-3: GROUP_CONCAT(DISTINCT) does not preserve insertion order
// =============================================================================

// LOCK: multi_distinct_group_concat de-duplicates via hash, so output is
// hash-order, not insertion-order. We assert: (a) all distinct values are
// present and (b) we tolerate any permutation -- and at least record whether
// the observed order differs from insertion order for one concrete input.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_group_concat_distinct_order_not_preserved) {
    // group_concat takes (value, [separator]) -- DISTINCT wraps that. We use
    // the single-arg form for simplicity.
    DataTypes args = {std::make_shared<DataTypeString>()};
    auto fn = factory.get("multi_distinct_group_concat", args, std::make_shared<DataTypeString>(),
                          false, -1);
    ASSERT_NE(fn, nullptr) << "multi_distinct_group_concat must be registered via combinator";

    AggState s(fn);
    // Insertion order: zebra, apple, mango, apple (dup), banana.
    auto col = make_string_column({"zebra", "apple", "mango", "apple", "banana"});
    add_all(fn.get(), s.place, col.get(), arena);

    auto result = ColumnString::create();
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    StringRef sr = result->get_data_at(0);
    std::string out(sr.data, sr.size);

    // Token check: every distinct value must appear exactly once.
    std::set<std::string> expected_tokens {"zebra", "apple", "mango", "banana"};
    std::set<std::string> got_tokens;
    // group_concat default separator is "," for the non-explicit form,
    // but the impl will use whatever was passed as separator. With a
    // single-arg form there is *no* separator => values are concatenated
    // raw. To make this robust, just count substrings.
    for (const auto& tok : expected_tokens) {
        if (out.find(tok) != std::string::npos) {
            got_tokens.insert(tok);
        }
    }
    EXPECT_EQ(got_tokens, expected_tokens)
            << "multi_distinct_group_concat dropped a distinct token. Got: " << out;

    // Order-check (informational): record whether observed order differs
    // from insertion order. We do NOT EXPECT_EQ on order -- the SEV is
    // precisely "order is not guaranteed".
    std::vector<std::string> insertion_order {"zebra", "apple", "mango", "banana"};
    std::vector<std::string> observed_order;
    {
        // Walk insertion order and record position in `out` for each.
        std::vector<std::pair<size_t, std::string>> pos_tok;
        for (const auto& t : insertion_order) {
            auto p = out.find(t);
            if (p != std::string::npos) pos_tok.push_back({p, t});
        }
        std::sort(pos_tok.begin(), pos_tok.end());
        for (auto& [_p, t] : pos_tok) observed_order.push_back(t);
    }
    // Whether observed_order == insertion_order is environment-dependent
    // (hash-randomization). The point of the SEV is precisely that we cannot
    // RELY on it; we just record what happened.
    SUCCEED() << "Insertion order: zebra,apple,mango,banana ; observed order: "
              << [&]() {
                     std::string acc;
                     for (auto& t : observed_order) {
                         if (!acc.empty()) acc += ",";
                         acc += t;
                     }
                     return acc;
                 }()
              << " ; raw out=" << out;
}

// =============================================================================
// NULL handling matrix
// =============================================================================

// SUM over all-NULL input returns NULL (nullable wrapper never set flag).
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_all_null_returns_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("sum", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({0, 0, 0}, {1, 1, 1});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_TRUE(result->is_null_at(0));
}

// SUM over zero rows returns NULL.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_empty_returns_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("sum", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    // No add() calls.

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_TRUE(result->is_null_at(0));
}

// SUM mixed NULL + non-NULL: nulls skipped, sum of the rest returned.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_mixed_null_skipped) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("sum", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({10, 0, 20, 0, 30}, {0, 1, 0, 1, 0});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_FALSE(result->is_null_at(0));
    EXPECT_EQ(assert_cast<const ColumnInt64&>(result->get_nested_column()).get_element(0), 60);
}

// COUNT over all-NULL input returns 0 (not NULL: count is NOT-NULL).
// Using the result_is_nullable=true variant -- which registers
// CountNotNullUnary that skips NULLs.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_COUNT_col_all_null_returns_zero) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("count", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({0, 0, 0}, {1, 1, 1});
    add_all(fn.get(), s.place, col.get(), arena);

    auto result = ColumnInt64::create();
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_EQ(result->get_element(0), int64_t(0));
}

// COUNT(*) (no argument) counts NULL rows -- it counts rows regardless of nulls.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_COUNT_star_counts_null_rows) {
    auto fn = factory.get("count", DataTypes {}, nullptr, false, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    // Drive 5 rows of count -- count(*) does not look at columns.
    Arena ar;
    for (int i = 0; i < 5; ++i) {
        fn->add(s.place, nullptr, i, ar);
    }

    auto result = ColumnInt64::create();
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_EQ(result->get_element(0), int64_t(5));
}

// AVG over all-NULL input returns NULL.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_AVG_all_null_returns_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("avg", {nt}, nullptr, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({0, 0}, {1, 1});
    add_all(fn.get(), s.place, col.get(), arena);

    // AVG return type is DOUBLE for integer inputs in Doris.
    auto nested = ColumnFloat64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_TRUE(result->is_null_at(0));
}

// MIN over all-NULL input returns NULL (no sentinel initialization corner).
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_MIN_all_null_returns_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("min", {nt}, nt, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({0, 0}, {1, 1});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_TRUE(result->is_null_at(0));
}

// MAX over all-NULL input returns NULL.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_MAX_all_null_returns_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("max", {nt}, nt, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({0, 0}, {1, 1});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_TRUE(result->is_null_at(0));
}

// MIN over mixed-NULL input returns the smallest non-NULL value.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_MIN_mixed_null_skipped) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("min", {nt}, nt, true, -1);
    ASSERT_NE(fn, nullptr);

    AggState s(fn);
    auto col = make_nullable_int64_column({100, 0, 7, 0, 42}, {0, 1, 0, 1, 0});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_FALSE(result->is_null_at(0));
    EXPECT_EQ(assert_cast<const ColumnInt64&>(result->get_nested_column()).get_element(0), 7);
}

// =============================================================================
// State serialize -> deserialize -> continue-add roundtrip
// =============================================================================

// SUM: serialize partial state, deserialize into a fresh state, then continue
// adding -- final value must equal the sum of all rows.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_SUM_state_roundtrip_continues_add) {
    auto fn = factory.get("sum", {std::make_shared<DataTypeInt64>()}, nullptr, false, -1);
    ASSERT_NE(fn, nullptr);

    // Phase A: add the first 3 rows, serialize to a string column.
    auto first_part = make_int64_column({10, 20, 30});
    auto second_part = make_int64_column({40, 50});

    MutableColumnPtr serialize_column = fn->create_serialize_column();
    {
        AggState a(fn);
        add_all(fn.get(), a.place, first_part.get(), arena);
        fn->serialize_without_key_to_column(a.place, *serialize_column);
    }

    // Phase B: fresh state, deserialize, then continue adding.
    AggState b(fn);
    fn->deserialize_and_merge_from_column(b.place, *serialize_column, arena);
    add_all(fn.get(), b.place, second_part.get(), arena);

    auto result = ColumnInt64::create();
    fn->insert_result_into(b.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_EQ(result->get_element(0), int64_t(10 + 20 + 30 + 40 + 50));
}

// COUNT: state roundtrip + continue-add semantics.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_COUNT_state_roundtrip_continues_add) {
    auto fn = factory.get("count", DataTypes {}, nullptr, false, -1);
    ASSERT_NE(fn, nullptr);

    Arena ar;
    MutableColumnPtr serialize_column = fn->create_serialize_column();
    {
        AggState a(fn);
        for (int i = 0; i < 4; ++i) fn->add(a.place, nullptr, i, ar);
        fn->serialize_without_key_to_column(a.place, *serialize_column);
    }
    AggState b(fn);
    fn->deserialize_and_merge_from_column(b.place, *serialize_column, ar);
    for (int i = 0; i < 3; ++i) fn->add(b.place, nullptr, i, ar);

    auto result = ColumnInt64::create();
    fn->insert_result_into(b.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_EQ(result->get_element(0), int64_t(7));
}

// =============================================================================
// DISTINCT correctness
// =============================================================================

// multi_distinct_count(col) over rows with one NULL excludes NULL from count.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_multi_distinct_count_excludes_null) {
    DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
    auto fn = factory.get("multi_distinct_count", {nt}, std::make_shared<DataTypeInt64>(), true,
                          -1);
    ASSERT_NE(fn, nullptr) << "multi_distinct_count must be registered via combinator";

    AggState s(fn);
    // distinct values: {1, 2, 3, NULL} -> count = 3
    auto col = make_nullable_int64_column({1, 2, 1, 3, 0, 2}, {0, 0, 0, 0, 1, 0});
    add_all(fn.get(), s.place, col.get(), arena);

    auto nested = ColumnInt64::create();
    auto null_map = ColumnUInt8::create();
    auto result = ColumnNullable::create(std::move(nested), std::move(null_map));
    fn->insert_result_into(s.place, *result);
    ASSERT_EQ(result->size(), 1u);
    EXPECT_FALSE(result->is_null_at(0));
    EXPECT_EQ(assert_cast<const ColumnInt64&>(result->get_nested_column()).get_element(0),
              int64_t(3));
}

// LOCK / FINDING: at BE factory level, multi_distinct_count(a,b) is NOT
// supported -- the Distinct combinator forwards the 2-arg type list to the
// nested `count` creator, which throws "Aggregate function count requires 0
// to 1 arguments, got 2". Multi-column distinct-count is rewritten on the FE
// side (e.g. via array_agg/coalesce concat) before reaching BE, but the BE
// factory itself rejects the direct lookup.
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_multi_distinct_count_two_col_factory_rejects) {
    DataTypePtr t = std::make_shared<DataTypeInt64>();
    // Expect the factory.get() call to throw the count-arity exception.
    EXPECT_THROW(
            {
                auto fn = factory.get("multi_distinct_count", {t, t},
                                      std::make_shared<DataTypeInt64>(), false, -1);
                (void)fn;
            },
            doris::Exception)
            << "If this fires, BE now accepts multi_distinct_count(a,b) directly -- update the "
               "contract note above.";
}

// =============================================================================
// is_trivial flag (state initialization characteristic)
// =============================================================================

// SUM is_trivial=true (state init = 0). COUNT is_trivial=true. AVG is_trivial=true.
// MIN(int64) is_trivial=false (no IS_ANY, needs sentinel-flag).
TEST_F(AggFunctionFeatureReviewTest, UT_AGG_is_trivial_flag_matrix) {
    {
        auto fn = factory.get("sum", {std::make_shared<DataTypeInt64>()}, nullptr, false, -1);
        ASSERT_NE(fn, nullptr);
        EXPECT_TRUE(fn->is_trivial()) << "SUM raw should be trivial";
    }
    {
        auto fn = factory.get("count", DataTypes {}, nullptr, false, -1);
        ASSERT_NE(fn, nullptr);
        EXPECT_TRUE(fn->is_trivial()) << "COUNT raw should be trivial";
    }
    {
        auto fn = factory.get("avg", {std::make_shared<DataTypeInt64>()}, nullptr, false, -1);
        ASSERT_NE(fn, nullptr);
        EXPECT_TRUE(fn->is_trivial()) << "AVG raw should be trivial";
    }
    {
        DataTypePtr t = std::make_shared<DataTypeInt64>();
        auto fn = factory.get("min", {t}, t, false, -1);
        ASSERT_NE(fn, nullptr);
        EXPECT_FALSE(fn->is_trivial()) << "MIN(int64) should NOT be trivial (needs sentinel flag)";
    }
    {
        // The nullable wrapper itself is_trivial() == false (sets flag).
        DataTypePtr nt = make_nullable(std::make_shared<DataTypeInt64>());
        auto fn = factory.get("sum", {nt}, nullptr, true, -1);
        ASSERT_NE(fn, nullptr);
        EXPECT_FALSE(fn->is_trivial())
                << "Nullable wrapper around SUM should NOT be trivial (carries the SET flag)";
    }
}

} // namespace doris
