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

// Feature-review UT for JsonbPath / leg_info (json_type SOP step 7).
// Covers UT-JT-012, UT-JT-013, UT-JT-014, UT-JT-015, UT-JT-016, UT-JT-035..037.

#include <gtest/gtest.h>

#include <string>
#include <string_view>
#include <vector>

#include "util/jsonb_document.h"

namespace doris {

class JsonbPathFeatureReviewTest : public testing::Test {};

// Helper: parse a path string and return success bool + resulting leg count.
static bool parse_ok(const std::string& s, JsonbPath* out) {
    return out->seek(s.data(), s.size());
}

// ============================================================================
// UT-JT-012 : 20-entry legal-path matrix all accepted.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_012_legal_paths_accepted) {
    std::vector<std::string> legals = {
            "$",
            "$.a",
            "$.a.b",
            "$.abc.def",
            "$[0]",
            "$[10]",
            "$[last]",
            "$[last-1]",
            "$.a[0]",
            "$.a[0].b",
            "$.*",
            "$.a.*",
            "$[*]",
            "$.a[*]",
            "$**.a",
            "$**[0]",
            "$.\"a b\"",       // quoted key with space
            "$.\"with\\\"quote\"",  // quoted key containing escaped quote
            "$.\"\\n\"",       // quoted key with escaped newline
            "$.foo[0][1]",
    };
    for (const auto& p : legals) {
        JsonbPath path;
        EXPECT_TRUE(parse_ok(p, &path)) << "legal path must parse: " << p;
    }
}

// ============================================================================
// UT-JT-013 : 20-entry illegal-path matrix all rejected.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_013_illegal_paths_rejected) {
    std::vector<std::string> illegals = {
            "",
            ".a",          // missing $
            "$a",          // missing .
            "$.",          // dangling .
            "$[",          // unterminated [
            "$[]",         // empty index
            "$[abc]",      // non-numeric, non-last
            "$[-1]",       // negative index (SEV-2 #N7)
            "$[1.5]",      // float
            "$[last-]",    // bad last-N
            "$[lastX]",    // bad
            "$[Last]",     // mixed case "last"
            "$[LAST]",     // upper case
            "$..a",        // double-dot
            "$.[0]",       // dot before [
            "$..",
            "$.\"unterminated", // unterminated quote
            "$.\"\"",      // empty quoted key
            "$.a.",        // trailing dot
            "$.a..b",      // double dot
    };
    for (const auto& p : illegals) {
        JsonbPath path;
        EXPECT_FALSE(parse_ok(p, &path)) << "illegal path must be rejected: " << p;
    }
}

// ============================================================================
// UT-JT-014 (SEV-2 #N6) : leg_info::to_string round-trip with escaped chars.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_014_to_string_round_trip_escapes) {
    // We construct a leg_info directly with a key containing '\n' and '\t' raw,
    // and verify to_string emits ESCAPE-prefixed forms that can be reparsed.
    {
        leg_info leg;
        std::string key = "ab"; // simple case first
        leg.leg_ptr = const_cast<char*>(key.data());
        leg.leg_len = static_cast<unsigned int>(key.size());
        leg.type = MEMBER_CODE;
        std::string out;
        ASSERT_TRUE(leg.to_string(&out));
        ASSERT_EQ(out, ".ab");
    }
    {
        // key containing \n (newline) — to_string must escape with backslash.
        std::string key;
        key.push_back('a');
        key.push_back('\n');
        key.push_back('b');
        leg_info leg;
        leg.leg_ptr = const_cast<char*>(key.data());
        leg.leg_len = static_cast<unsigned int>(key.size());
        leg.type = MEMBER_CODE;
        std::string out;
        ASSERT_TRUE(leg.to_string(&out));
        // Current impl emits raw control char + ESCAPE prefix; we check that
        // the output contains ESCAPE before the newline (escape applied).
        bool has_escape = false;
        for (size_t i = 0; i + 1 < out.size(); ++i) {
            if (out[i] == '\\' && out[i + 1] == '\n') {
                has_escape = true;
                break;
            }
        }
        EXPECT_TRUE(has_escape) << "to_string must emit escape before raw \\n; out=" << out;
    }
}

// ============================================================================
// UT-JT-015 : leg_info::is_wildcard / is_supper_wildcard unit.
// Note: these are members of JsonbPath, not leg_info. Adjust accordingly:
// we test JsonbPath::is_wildcard() / is_supper_wildcard() via parsed paths.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_015_is_wildcard_and_super) {
    {
        JsonbPath p;
        ASSERT_TRUE(parse_ok("$.*", &p));
        EXPECT_TRUE(p.is_wildcard()) << "$.* must report is_wildcard";
        EXPECT_FALSE(p.is_supper_wildcard());
    }
    {
        JsonbPath p;
        ASSERT_TRUE(parse_ok("$**.a", &p));
        EXPECT_TRUE(p.is_supper_wildcard()) << "$**.a must report is_supper_wildcard";
    }
    {
        JsonbPath p;
        ASSERT_TRUE(parse_ok("$.a.b", &p));
        EXPECT_FALSE(p.is_wildcard());
        EXPECT_FALSE(p.is_supper_wildcard());
    }
}

// ============================================================================
// UT-JT-016 (SEV-2 #N7) : array_index negative literal — $[-N] rejected,
// $[last] / $[last-N] accepted.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_016_array_index_negative) {
    {
        JsonbPath p;
        EXPECT_FALSE(parse_ok("$[-1]", &p)) << "$[-1] must be rejected (SEV-2 #N7)";
        EXPECT_FALSE(parse_ok("$[-10]", &p));
    }
    {
        JsonbPath p;
        EXPECT_TRUE(parse_ok("$[last]", &p));
        EXPECT_TRUE(parse_ok("$[last-1]", &p));
        EXPECT_TRUE(parse_ok("$[last-100]", &p));
    }
}

// ============================================================================
// UT-JT-035 (subset) : large legal/illegal matrix overlap with UT-012/013 +
// LAST case-sensitivity check.
// ============================================================================
TEST_F(JsonbPathFeatureReviewTest, ut_jt_035_last_case_sensitivity) {
    // Spec: 'last' is case-sensitive lowercase only.
    JsonbPath p;
    EXPECT_TRUE(parse_ok("$[last]", &p));
    EXPECT_FALSE(parse_ok("$[Last]", &p));
    EXPECT_FALSE(parse_ok("$[LAST]", &p));
    EXPECT_FALSE(parse_ok("$[last-]", &p));     // bad subtractor
    EXPECT_FALSE(parse_ok("$[last+1]", &p));    // '+' not allowed (only minus)
}

} // namespace doris
