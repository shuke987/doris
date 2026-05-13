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

// Feature review UT for ColumnStruct, covering:
//   - UT-CT-CS-002: sanity_check on mismatched sub-column sizes
//   - UT-CT-CS-007 (SEV-3 #N11): less::operator() uses *col.get() as RHS (intra-column self compare)
//   - UT-CT-025 (SEV-2 #4 / N7): compare_at lex by min_size then by size for tuples of unequal length
//   - UT-CT-CL-006: compare_at field-position-sensitive
//   - UT-CT-026: serialize/deserialize round-trip
//
// Hard-spec contract: PASS = spec; FAIL = bug reproduced.

#include <gtest/gtest.h>

#include <memory>

#include "core/column/column_struct.h"
#include "core/column/column_vector.h"

namespace doris {

class ColumnStructFeatureReviewTest : public ::testing::Test {};

static MutableColumnPtr make_int_column(const std::vector<Int32>& vs) {
    auto c = ColumnInt32::create();
    for (Int32 v : vs) c->insert_value(v);
    return c;
}

// ------------------------------------------------------------------
// UT-CT-CS-002: sanity_check enforces consistent sub-column sizes.
// ------------------------------------------------------------------
TEST_F(ColumnStructFeatureReviewTest, UT_CT_CS_002_SanityCheckEnforcesSubColumnSize) {
    MutableColumns cols;
    cols.push_back(make_int_column({1, 2, 3}));
    cols.push_back(make_int_column({10, 20})); // mismatched size
    auto st = ColumnStruct::create(std::move(cols));
    EXPECT_ANY_THROW(st->sanity_check())
            << "ColumnStruct::sanity_check must throw on mismatched sub-column sizes";
}

// ------------------------------------------------------------------
// UT-CT-CL-006: compare_at field-position-sensitive same-shape.
//   row a = (1, 10), row b = (1, 20)  ->  cmp < 0 (second field differs)
//   row a = (2, 10), row b = (1, 99)  ->  cmp > 0 (first field decides)
// ------------------------------------------------------------------
TEST_F(ColumnStructFeatureReviewTest, UT_CT_CL_006_CompareAtFieldPositionSensitive) {
    MutableColumns a_cols;
    a_cols.push_back(make_int_column({1, 2}));
    a_cols.push_back(make_int_column({10, 10}));
    auto a = ColumnStruct::create(std::move(a_cols));

    MutableColumns b_cols;
    b_cols.push_back(make_int_column({1, 1}));
    b_cols.push_back(make_int_column({20, 99}));
    auto b = ColumnStruct::create(std::move(b_cols));

    EXPECT_LT(a->compare_at(0, 0, *b, 1), 0)
            << "row a[0]=(1,10) < b[0]=(1,20) (second field decides)";
    EXPECT_GT(a->compare_at(1, 1, *b, 1), 0)
            << "row a[1]=(2,10) > b[1]=(1,99) (first field decides)";
}

// ------------------------------------------------------------------
// UT-CT-025 (SEV-2 #4 / N7): compare_at across tuples of unequal length.
//   Spec: compare min_size fields first; if equal in the common prefix, longer wins.
//   We build a 2-field struct and a 3-field struct (same prefix), then compare.
// ------------------------------------------------------------------
TEST_F(ColumnStructFeatureReviewTest, UT_CT_025_CompareAtTuplesOfUnequalLength) {
    // 2-field struct: (1, 10)
    MutableColumns short_cols;
    short_cols.push_back(make_int_column({1}));
    short_cols.push_back(make_int_column({10}));
    auto short_st = ColumnStruct::create(std::move(short_cols));

    // 3-field struct: (1, 10, 100)  -- same prefix
    MutableColumns long_cols;
    long_cols.push_back(make_int_column({1}));
    long_cols.push_back(make_int_column({10}));
    long_cols.push_back(make_int_column({100}));
    auto long_st = ColumnStruct::create(std::move(long_cols));

    EXPECT_LT(short_st->compare_at(0, 0, *long_st, 1), 0)
            << "Shorter tuple < longer tuple when prefix is equal";
    EXPECT_GT(long_st->compare_at(0, 0, *short_st, 1), 0)
            << "Longer tuple > shorter tuple when prefix is equal (symmetric)";
}

// ------------------------------------------------------------------
// UT-CT-026: serialize/deserialize round-trip on simple int struct.
// ------------------------------------------------------------------
TEST_F(ColumnStructFeatureReviewTest, UT_CT_026_SerializeDeserializeRoundTrip) {
    MutableColumns cols;
    cols.push_back(make_int_column({1}));
    cols.push_back(make_int_column({10}));
    auto src = ColumnStruct::create(std::move(cols));

    size_t bytes = src->serialize_size_at(0);
    std::vector<char> buf(bytes + 1, 0);
    size_t wrote = src->serialize_impl(buf.data(), 0);
    EXPECT_EQ(wrote, bytes);

    // Build a fresh empty struct of the same shape
    MutableColumns dst_cols;
    dst_cols.push_back(ColumnInt32::create());
    dst_cols.push_back(ColumnInt32::create());
    auto dst = ColumnStruct::create(std::move(dst_cols));
    dst->deserialize_impl(buf.data());
    EXPECT_EQ(dst->size(), 1u);
    EXPECT_EQ(src->compare_at(0, 0, *dst, 1), 0)
            << "Round-tripped ColumnStruct must equal source";
}

// ------------------------------------------------------------------
// UT-CT-CS-007 (SEV-3 #N11): less::operator() with same column on both sides for
// intra-column sort works.
//
// We can't directly access the private `less` template, but we can drive the same
// path via compare_at with the column compared against itself — that's the same
// pattern the `less` op-functor uses internally (*col.get() as RHS).
// ------------------------------------------------------------------
TEST_F(ColumnStructFeatureReviewTest, UT_CT_CS_007_CompareAtSelfRhsIsConsistent) {
    MutableColumns cols;
    cols.push_back(make_int_column({3, 1, 2}));
    cols.push_back(make_int_column({30, 10, 20}));
    auto st = ColumnStruct::create(std::move(cols));

    // intra-column compare: every row compared to itself returns 0
    for (size_t i = 0; i < 3; ++i) {
        EXPECT_EQ(st->compare_at(i, i, *st, 1), 0)
                << "self-compare at row " << i << " must be 0";
    }
    // cross-row inside the same column
    EXPECT_GT(st->compare_at(0, 1, *st, 1), 0) << "(3,30) > (1,10)";
    EXPECT_LT(st->compare_at(1, 2, *st, 1), 0) << "(1,10) < (2,20)";
}

} // namespace doris
