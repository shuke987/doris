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

// Feature review UT for DataTypeStruct, covering the SEV-2 #N6 / #N7 bug surfaces
// identified in feature-review/complex_types/step52_review.md.
//
// Hard-spec assertion contract: PASS = spec-conformant; FAIL = bug reproduced
// (no baseline-pin).

#include <gtest/gtest.h>

#include <memory>
#include <vector>

#include "core/data_type/data_type_struct.h"
#include "core/data_type/data_type_number.h"
#include "core/data_type/data_type_string.h"

namespace doris {

class DataTypeStructFeatureReviewTest : public ::testing::Test {};

// ------------------------------------------------------------------
// UT-CT-CS-003 (SEV-2 #N6): ctor must reject empty field names.
//
// Spec (post-fix): DataTypeStruct(elems, names) where any name is "" must throw
// (or otherwise refuse construction). Current branch-4.1 baseline silently
// discards the Status returned by `check_tuple_names`, so empty names are
// accepted; FAIL here == SEV-2 #N6 reproduced.
// ------------------------------------------------------------------
TEST_F(DataTypeStructFeatureReviewTest, UT_CT_CS_003_EmptyFieldNameRejected) {
    DataTypes elems = {std::make_shared<DataTypeInt32>()};
    Strings names = {""};
    // Hard spec assertion: throws. Current branch-4.1: no throw -> SEV-2 #N6 reproduced.
    EXPECT_ANY_THROW({ DataTypeStruct ds(elems, names); })
            << "DataTypeStruct must reject empty field names (SEV-2 #N6)";
}

// ------------------------------------------------------------------
// UT-CT-CS-004 (SEV-2 #N6): ctor must reject duplicate field names.
//
// Spec (post-fix): DataTypeStruct(elems, {"a","a"}) throws.
// Current branch-4.1: check_tuple_names returns Status::InvalidArgument but the
// caller discards it -> FAIL here == bug reproduced.
// ------------------------------------------------------------------
TEST_F(DataTypeStructFeatureReviewTest, UT_CT_CS_004_DuplicateFieldNamesRejected) {
    DataTypes elems = {std::make_shared<DataTypeInt32>(), std::make_shared<DataTypeInt32>()};
    Strings names = {"a", "a"};
    EXPECT_ANY_THROW({ DataTypeStruct ds(elems, names); })
            << "DataTypeStruct must reject duplicate field names (SEV-2 #N6)";
}

// ------------------------------------------------------------------
// UT-CT-CS-005 (SEV-2 #N7): try_get_position_by_name is case-SENSITIVE on BE side.
//
// Spec assertion: stored name "AA" must NOT match lookup "Aa". This locks down
// the BE-side case-sensitivity contract that FE side breaks by lowercasing names
// in fe-common StructField -- the cross-system divergence is the bug (see FE
// StructTypeFeatureReviewTest).
// ------------------------------------------------------------------
TEST_F(DataTypeStructFeatureReviewTest, UT_CT_CS_005_TryGetPositionByNameIsCaseSensitive) {
    DataTypes elems = {std::make_shared<DataTypeInt32>()};
    Strings names = {"AA"};
    DataTypeStruct ds(elems, names);

    auto pos_exact = ds.try_get_position_by_name("AA");
    EXPECT_TRUE(pos_exact.has_value()) << "Exact case match should succeed";
    EXPECT_EQ(pos_exact.value(), 0u);

    auto pos_mixed = ds.try_get_position_by_name("Aa");
    EXPECT_FALSE(pos_mixed.has_value())
            << "BE try_get_position_by_name is case-sensitive: 'AA' != 'Aa'";

    auto pos_lower = ds.try_get_position_by_name("aa");
    EXPECT_FALSE(pos_lower.has_value())
            << "BE try_get_position_by_name is case-sensitive: 'AA' != 'aa'";
}

// ------------------------------------------------------------------
// UT-CT-CS-001 (BE-companion): auto-named struct numbers fields starting at "1".
// ------------------------------------------------------------------
TEST_F(DataTypeStructFeatureReviewTest, UT_CT_CS_001_AutoNamedFieldsStartAtOne) {
    DataTypes elems = {std::make_shared<DataTypeInt32>(),
                       std::make_shared<DataTypeString>(),
                       std::make_shared<DataTypeInt64>()};
    DataTypeStruct ds(elems);  // no names -> auto-assign

    auto p1 = ds.try_get_position_by_name("1");
    auto p2 = ds.try_get_position_by_name("2");
    auto p3 = ds.try_get_position_by_name("3");
    auto p0 = ds.try_get_position_by_name("0");

    EXPECT_TRUE(p1.has_value()) << "Auto-name '1' must be present";
    EXPECT_EQ(p1.value(), 0u);
    EXPECT_TRUE(p2.has_value());
    EXPECT_EQ(p2.value(), 1u);
    EXPECT_TRUE(p3.has_value());
    EXPECT_EQ(p3.value(), 2u);
    EXPECT_FALSE(p0.has_value()) << "No '0' in auto-named struct (positions are 1-based)";
}

// ------------------------------------------------------------------
// UT-CT-CS-002 (BE-companion): explicit names ctor with wrong-size names throws.
// Documents the existing valid throw path; PASS on baseline.
// ------------------------------------------------------------------
TEST_F(DataTypeStructFeatureReviewTest, UT_CT_CS_002_NamesSizeMismatchThrows) {
    DataTypes elems = {std::make_shared<DataTypeInt32>(),
                       std::make_shared<DataTypeInt32>()};
    Strings names = {"only_one_name"};
    EXPECT_ANY_THROW({ DataTypeStruct ds(elems, names); });
}

} // namespace doris
