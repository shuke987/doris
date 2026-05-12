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

// Feature-review UT for JsonbWriter writeKey / writeInt boundary
// (json_type SOP step 7).
// Reproduces and guards: SEV-3 #N13 (writeKey embedded NUL truncation) plus
// writeInt narrowest-type dispatch. This is a separately-targetable test for
// quick triage; the same test ids are also referenced in
// jsonb_document_feature_review_test.cpp where applicable.

#include <gtest/gtest.h>

#include <cstdint>
#include <limits>

#include "util/jsonb_document.h"
#include "util/jsonb_writer.h"

namespace doris {

class JsonbWriterFeatureReviewTest : public testing::Test {};

// ============================================================================
// UT-JT-028 (SEV-3 #N13): writeKey(const char*) silent truncation contract.
// ============================================================================
TEST_F(JsonbWriterFeatureReviewTest, ut_jt_028_writekey_const_char_strlen_truncates) {
    const char k[] = "abc\0def";  // strlen == 3
    JsonbWriter w;
    ASSERT_TRUE(w.writeStartObject());
    ASSERT_TRUE(w.writeKey(k));  // strlen path
    ASSERT_TRUE(w.writeInt(int64_t(1)));
    ASSERT_TRUE(w.writeEndObject());
    const auto* v = w.getValue();
    ASSERT_TRUE(v->isObject());
    const auto* obj = v->unpack<ObjectVal>();
    auto it = obj->begin();
    // Spec post-fix: writeKey(const char*) is deprecated/rejected. We assert
    // the post-fix contract (klen reflects intended key length 7, or write
    // should have failed). Current baseline truncates to 3 -> FAIL == SEV-3
    // #N13 reproduced.
    EXPECT_NE(it->klen(), 3)
            << "writeKey(const char*) silently truncated embedded NUL "
               "(SEV-3 #N13 not yet fixed)";
}

// ============================================================================
// UT-JT-040: writeInt boundary minimal-type dispatch (extended cases).
// ============================================================================
TEST_F(JsonbWriterFeatureReviewTest, ut_jt_040_writeint_dispatch_extended) {
    struct Tc {
        int64_t in;
        JsonbType expected;
        const char* tag;
    };
    Tc matrix[] = {
            {0, JsonbType::T_Int8, "zero"},
            {-1, JsonbType::T_Int8, "minus one"},
            {static_cast<int64_t>(std::numeric_limits<int8_t>::max()), JsonbType::T_Int8, "i8max"},
            {static_cast<int64_t>(std::numeric_limits<int8_t>::min()), JsonbType::T_Int8, "i8min"},
            {static_cast<int64_t>(std::numeric_limits<int8_t>::max()) + 1,
             JsonbType::T_Int16, "i8max+1"},
            {static_cast<int64_t>(std::numeric_limits<int16_t>::max()),
             JsonbType::T_Int16, "i16max"},
            {static_cast<int64_t>(std::numeric_limits<int16_t>::min()),
             JsonbType::T_Int16, "i16min"},
            {static_cast<int64_t>(std::numeric_limits<int16_t>::max()) + 1,
             JsonbType::T_Int32, "i16max+1"},
            {static_cast<int64_t>(std::numeric_limits<int32_t>::max()),
             JsonbType::T_Int32, "i32max"},
            {static_cast<int64_t>(std::numeric_limits<int32_t>::min()),
             JsonbType::T_Int32, "i32min"},
            {static_cast<int64_t>(std::numeric_limits<int32_t>::max()) + 1LL,
             JsonbType::T_Int64, "i32max+1"},
            {std::numeric_limits<int64_t>::max(), JsonbType::T_Int64, "i64max"},
            {std::numeric_limits<int64_t>::min(), JsonbType::T_Int64, "i64min"},
    };
    for (const auto& tc : matrix) {
        JsonbWriter w;
        ASSERT_TRUE(w.writeInt(tc.in)) << "writeInt failed at " << tc.tag;
        const auto* v = w.getValue();
        ASSERT_NE(v, nullptr);
        ASSERT_EQ(v->type, tc.expected)
                << "writeInt(" << static_cast<long long>(tc.in) << ") tag=" << tc.tag
                << " expected type " << static_cast<int>(tc.expected) << " got "
                << static_cast<int>(v->type);
    }
}

} // namespace doris
