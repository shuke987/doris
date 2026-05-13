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

// Feature review UT for ColumnMap, covering:
//   - UT-CT-021 (SEV-3 #10) / UT-CT-CL-005: position-sensitive lex compare_at
//   - UT-CT-022 (SEV-3 #9): empty-map vs non-empty hash differs (size-0 seed)
//
// Hard-spec contract: PASS = spec-conformant; FAIL = bug.

#include <gtest/gtest.h>

#include "core/column/column_array.h"
#include "core/column/column_map.h"
#include "core/column/column_nullable.h"
#include "core/column/column_vector.h"

namespace doris {

class ColumnMapFeatureReviewTest : public ::testing::Test {};

// Helper: build ColumnMap<Nullable<Int32>, Int32> from per-row key/value lists.
static MutableColumnPtr build_int_map_column(
        const std::vector<std::vector<std::pair<Int32, Int32>>>& rows) {
    auto keys = ColumnNullable::create(ColumnInt32::create(), ColumnUInt8::create());
    auto values = ColumnInt32::create();
    auto offsets = ColumnArray::ColumnOffsets::create();
    UInt64 offset = 0;
    for (const auto& row : rows) {
        for (const auto& [k, v] : row) {
            keys->insert(Field::create_field<TYPE_INT>(k));
            values->insert_value(v);
        }
        offset += row.size();
        offsets->get_data().push_back(offset);
    }
    return ColumnMap::create(std::move(keys), std::move(values), std::move(offsets));
}

// ------------------------------------------------------------------
// UT-CT-021 / UT-CT-CL-005 (SEV-3 #10): position-sensitive lex compare.
//
// Two maps with same keys in different insertion order are NOT equal:
//   {1:a, 2:b} vs {2:b, 1:a}  ->  compare_at != 0
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_021_CompareAtIsPositionSensitive) {
    auto a = build_int_map_column({{{1, 10}, {2, 20}}});
    auto b = build_int_map_column({{{2, 20}, {1, 10}}});
    int res = a->compare_at(0, 0, *b, 1);
    EXPECT_NE(res, 0)
            << "ColumnMap::compare_at is position-sensitive: "
               "{1:10,2:20} vs {2:20,1:10} must differ (SEV-3 #10)";
}

// ------------------------------------------------------------------
// UT-CT-021.b: equal maps compare as 0.
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_021_EqualMapsCompareZero) {
    auto a = build_int_map_column({{{1, 10}, {2, 20}}});
    auto b = build_int_map_column({{{1, 10}, {2, 20}}});
    EXPECT_EQ(a->compare_at(0, 0, *b, 1), 0);
}

// ------------------------------------------------------------------
// UT-CT-021.c: different size maps -> sign reflects size.
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_021_DifferentSizeCompareReflectsSize) {
    auto shorter = build_int_map_column({{{1, 10}}});
    auto longer = build_int_map_column({{{1, 10}, {2, 20}}});
    EXPECT_LT(shorter->compare_at(0, 0, *longer, 1), 0);
    EXPECT_GT(longer->compare_at(0, 0, *shorter, 1), 0);
}

// ------------------------------------------------------------------
// UT-CT-022 (SEV-3 #9): empty map seeded with sizeof(kv_size) hash; non-empty differs.
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_022_EmptyMapHashDiffersFromNonEmpty) {
    auto empty = build_int_map_column({{}});
    auto non_empty = build_int_map_column({{{1, 10}}});
    uint64_t h_empty = 0;
    uint64_t h_non = 0;
    empty->update_xxHash_with_value(0, 1, h_empty, nullptr);
    non_empty->update_xxHash_with_value(0, 1, h_non, nullptr);
    EXPECT_NE(h_empty, h_non)
            << "Empty map hash differs from non-empty map hash (SEV-3 #9 spec)";
}

// ------------------------------------------------------------------
// UT-CT-022.b: two empty maps hash the same way (deterministic).
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_022_TwoEmptyMapsSameHash) {
    auto a = build_int_map_column({{}});
    auto b = build_int_map_column({{}});
    uint64_t ha = 0;
    uint64_t hb = 0;
    a->update_xxHash_with_value(0, 1, ha, nullptr);
    b->update_xxHash_with_value(0, 1, hb, nullptr);
    EXPECT_EQ(ha, hb) << "Two empty maps must hash to the same value (determinism)";
}

// ------------------------------------------------------------------
// UT-CT-CM-002: keys_column must be nullable (sanity check enforces this).
//
// We can't easily assert sanity_check throws in a single shot, but we can verify
// that get_keys() returns a column whose nullability matches construction.
// ------------------------------------------------------------------
TEST_F(ColumnMapFeatureReviewTest, UT_CT_CM_002_KeysColumnNullable) {
    auto col = build_int_map_column({{{1, 10}}});
    auto* mp = assert_cast<const ColumnMap*>(col.get());
    EXPECT_TRUE(is_column_nullable(mp->get_keys()))
            << "ColumnMap::get_keys() must return a nullable column";
}

} // namespace doris
