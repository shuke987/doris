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

// Feature review UT for ColumnArray, focused on:
//   - UT-CT-CA-001 / UT-CT-CA-005: serialize_impl / deserialize_impl round-trip
//   - UT-CT-CA-003 / UT-CT-CA-006: get_number_of_dimensions deep nesting (no stack overflow)
//   - UT-CT-CA-004: operator[] InvalidArgument when array exceeds max_array_size_as_field
//   - UT-CT-CL-007: update_xxHash_with_value distinguishes empty [] from [NULL]
//
// Hard-spec contract: PASS = spec-conformant; FAIL = bug reproduced.

#include <gtest/gtest.h>

#include <memory>

#include "core/column/column_array.h"
#include "core/column/column_nullable.h"
#include "core/column/column_vector.h"
#include "core/types.h"

namespace doris {

class ColumnArrayFeatureReviewTest : public ::testing::Test {};

// Helper: build a ColumnArray<Int32> with the given values; offsets[-1] is 0 by
// PODArray convention.
static MutableColumnPtr build_int_array_column(
        const std::vector<std::vector<Int32>>& rows) {
    auto nested = ColumnInt32::create();
    auto offsets = ColumnArray::ColumnOffsets::create();
    UInt64 offset = 0;
    for (const auto& row : rows) {
        for (Int32 v : row) {
            nested->insert_value(v);
        }
        offset += row.size();
        offsets->get_data().push_back(offset);
    }
    return ColumnArray::create(std::move(nested), std::move(offsets));
}

// ------------------------------------------------------------------
// UT-CT-CA-002: offsets[-1] = 0 PODArray zero-fill -> empty array row offset_at(0) == 0.
// ------------------------------------------------------------------
TEST_F(ColumnArrayFeatureReviewTest, UT_CT_CA_002_EmptyArrayOffsetAtZeroIsZero) {
    auto col = build_int_array_column({{}});  // one empty row
    auto* arr = assert_cast<const ColumnArray*>(col.get());
    EXPECT_EQ(arr->offset_at(0), 0u)
            << "Empty array row at index 0 should have offset 0 (PODArray zero-fills offsets[-1])";
    EXPECT_EQ(arr->size_at(0), 0u);
}

// ------------------------------------------------------------------
// UT-CT-CA-003 / UT-CT-CA-006 (SEV-1 #1): get_number_of_dimensions 10-level nesting
// must NOT stack overflow.
//
// The current implementation is tail-recursive but the spec contract is the depth must
// be reportable without crash. Build ARRAY<ARRAY<...<INT>>> with 10 nesting levels.
// ------------------------------------------------------------------
TEST_F(ColumnArrayFeatureReviewTest, UT_CT_CA_003_GetDimensionsTenLevelNoCrash) {
    // Build 10-deep nested array columns:
    //   inner = ColumnInt32 (level 0)
    //   wrap with ColumnArray 10 times
    ColumnPtr inner = ColumnInt32::create();
    for (int i = 0; i < 10; ++i) {
        auto offsets = ColumnArray::ColumnOffsets::create();
        offsets->get_data().push_back(0);  // one empty row at each level
        inner = ColumnArray::create(inner->assume_mutable(), std::move(offsets));
    }
    auto* arr = assert_cast<const ColumnArray*>(inner.get());
    size_t dim = 0;
    EXPECT_NO_THROW(dim = arr->get_number_of_dimensions());
    EXPECT_EQ(dim, 10u) << "Ten levels of nested ColumnArray -> dimension 10";
}

// ------------------------------------------------------------------
// UT-CT-CA-004 (SEV-1 #2): operator[](row of size > max_array_size_as_field)
// throws InvalidArgument.
//
// max_array_size_as_field = 1,000,000. Build one row of 1,000,001 elements.
// ------------------------------------------------------------------
TEST_F(ColumnArrayFeatureReviewTest, UT_CT_CA_004_OperatorIndexThrowsOnHugeRow) {
    auto nested = ColumnInt32::create();
    auto offsets = ColumnArray::ColumnOffsets::create();
    constexpr size_t huge = 1'000'001;
    // Don't actually insert 1M+1 ints (too slow / RAM-heavy); just set offset to
    // declare the size. Pre-fill nested data with one value and an offset claiming
    // huge size. That's sufficient for operator[] to compute size > max and throw
    // BEFORE iterating the inner loop... wait, current impl pre-allocates Array(size)
    // and then loops. Let's stay safe and use a moderately huge size (1,000,001) with
    // pre-filled zeros: zero-init is fast.
    nested->get_data().resize_fill(huge, 0);
    offsets->get_data().push_back(huge);
    auto col = ColumnArray::create(std::move(nested), std::move(offsets));
    EXPECT_ANY_THROW({ (void)(*col)[0]; })
            << "operator[] on row exceeding max_array_size_as_field must throw InvalidArgument";
}

// ------------------------------------------------------------------
// UT-CT-CL-007 (SEV-3 #9): update_xxHash_with_value distinguishes empty [] from [NULL].
//
// Spec: hash([]) != hash([NULL]). Current implementation: empty array seeds with size=0
// (good), non-empty recurses into data layer where NULL element contributes a known
// hash byte; so PASS expected.
// ------------------------------------------------------------------
TEST_F(ColumnArrayFeatureReviewTest, UT_CT_CL_007_HashEmptyVsArrayContainingNullDiffers) {
    // Build [empty_array] -> ColumnArray with one row, no elements
    auto col_empty = build_int_array_column({{}});
    // Build [[NULL]] -> nullable inner with one row containing a single NULL
    auto inner_nullable = ColumnNullable::create(ColumnInt32::create(), ColumnUInt8::create());
    inner_nullable->insert_default();  // one NULL
    auto offsets_one = ColumnArray::ColumnOffsets::create();
    offsets_one->get_data().push_back(1);
    auto col_one_null = ColumnArray::create(std::move(inner_nullable), std::move(offsets_one));

    uint64_t h_empty = 0;
    uint64_t h_null = 0;
    col_empty->update_xxHash_with_value(0, 1, h_empty, nullptr);
    col_one_null->update_xxHash_with_value(0, 1, h_null, nullptr);
    EXPECT_NE(h_empty, h_null)
            << "Empty array [] must hash differently from [NULL] (SEV-3 #9 spec)";
}

// ------------------------------------------------------------------
// UT-CT-CA-001 / UT-CT-CA-005: serialize_impl + deserialize_impl round-trip
// with NULL element inside the nested column.
// ------------------------------------------------------------------
TEST_F(ColumnArrayFeatureReviewTest, UT_CT_CA_001_005_SerializeRoundTripWithNullElement) {
    // Build [1, NULL, 3] as a nullable int column, then one ColumnArray row of size 3.
    auto inner = ColumnNullable::create(ColumnInt32::create(), ColumnUInt8::create());
    inner->insert(Field::create_field<TYPE_INT>(static_cast<Int32>(1)));
    inner->insert_default();  // NULL
    inner->insert(Field::create_field<TYPE_INT>(static_cast<Int32>(3)));
    auto offsets = ColumnArray::ColumnOffsets::create();
    offsets->get_data().push_back(3);
    auto src = ColumnArray::create(std::move(inner), std::move(offsets));

    // Serialize row 0
    size_t bytes = src->serialize_size_at(0);
    std::vector<char> buf(bytes + 1, 0);
    size_t wrote = src->serialize_impl(buf.data(), 0);
    EXPECT_EQ(wrote, bytes)
            << "serialize_impl returned size differs from serialize_size_at";

    // Deserialize into a fresh ColumnArray
    auto inner2 = ColumnNullable::create(ColumnInt32::create(), ColumnUInt8::create());
    auto offsets2 = ColumnArray::ColumnOffsets::create();
    auto dst = ColumnArray::create(std::move(inner2), std::move(offsets2));
    dst->deserialize_impl(buf.data());
    EXPECT_EQ(dst->size(), 1u);
    EXPECT_EQ(dst->size_at(0), 3u);
    // Compare element-wise
    EXPECT_EQ(src->compare_at(0, 0, *dst, 1), 0)
            << "Round-tripped ColumnArray must compare equal to source";
}

} // namespace doris
