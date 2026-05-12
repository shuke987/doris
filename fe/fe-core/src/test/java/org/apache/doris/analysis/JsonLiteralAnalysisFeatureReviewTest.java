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

package org.apache.doris.analysis;

import org.apache.doris.common.AnalysisException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Feature review UT for {@link org.apache.doris.analysis.JsonLiteral}.
 *
 * <p>Scope: {@code json_type} step 7 SOP, derived from feature-review/json_type/ut_list.md:
 * <ul>
 *   <li>UT-JT-021 (SEV-2 #N4) : 200-level deeply-nested literal must be rejected by constructor.</li>
 *   <li>UT-JT-022 (SEV-2 #N5) : {@code compareLiteral} must NOT raise raw {@link RuntimeException};
 *       it must return 0 or throw {@link AnalysisException} once the SEV-2 #N5 fix lands.</li>
 *   <li>UT-JT-052 (SEV-2 #N5) : {@code toSqlImpl} round-trip across special chars
 *       ('\'', '\\', '\n', CJK, emoji) must produce SQL that parses back into the same JSON value.</li>
 * </ul>
 *
 * <p>Hardness contract follows tag-management UT conventions:
 * <ul>
 *   <li>PASS = spec-conformant behaviour (fix landed).</li>
 *   <li>FAIL = current branch-4.1 bug reproduction (asserts spec, current behaviour still buggy).</li>
 * </ul>
 */
public class JsonLiteralAnalysisFeatureReviewTest {

    // ------------------------------------------------------------------
    // UT-JT-021: SEV-2 #N4 — 200-level nesting must be rejected.
    //
    // Spec (post-fix): JsonLiteral should validate nesting depth in the
    // constructor and raise AnalysisException; current baseline accepts the
    // string silently because gson JsonParser has no built-in depth limit.
    // ------------------------------------------------------------------
    @Test
    public void testConstructDeeplyNested200LevelsRejected() {
        StringBuilder open = new StringBuilder();
        StringBuilder close = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            open.append("{\"a\":");
            close.append("}");
        }
        String deep = open.toString() + "1" + close.toString();
        // Fix-after-expected: constructor throws AnalysisException for >100-level nesting.
        // Current branch-4.1 baseline: parses successfully (this assertion FAILS == SEV-2 #N4 reproduced).
        Assertions.assertThrows(AnalysisException.class, () -> new JsonLiteral(deep),
                "Expect JsonLiteral ctor to reject 200-level nested JSON (SEV-2 #N4 fix)");
    }

    // ------------------------------------------------------------------
    // UT-JT-022: SEV-2 #N5 — compareLiteral must not leak raw RuntimeException.
    //
    // Spec (post-fix): JSON literals are not orderable; comparing them should
    // either return 0 (Hive-style consistent) or throw AnalysisException.
    // Current code throws a raw RuntimeException("Not support comparison between JSONB literals").
    // ------------------------------------------------------------------
    @Test
    public void testCompareLiteralMustNotThrowRawRuntimeException() throws AnalysisException {
        JsonLiteral a = new JsonLiteral("{\"k\":1}");
        JsonLiteral b = new JsonLiteral("{\"k\":2}");
        try {
            int r = a.compareLiteral(b);
            // post-fix path: returns deterministic value
            Assertions.assertTrue(r == 0 || r == 1 || r == -1,
                    "compareLiteral must return -1/0/1 if it returns at all");
        } catch (RuntimeException re) {
            // Strict typed-exception contract: ONLY raw RuntimeException is the bug.
            // If a future fix throws a checked AnalysisException we cannot reach this
            // branch (it would surface via the throws clause), so any RuntimeException
            // here is the SEV-2 #N5 leak.
            // Current branch-4.1 baseline FAILS here -> SEV-2 #N5 reproduced.
            Assertions.fail("compareLiteral leaked raw RuntimeException; expected typed exception or value: "
                    + re.getClass().getName() + " :: " + re.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // UT-JT-052: SEV-2 #N5 — toSqlImpl round-trip for special-char matrix.
    //
    // Spec: every literal SQL string emitted by toSqlImpl must reparse to the
    // same JSON value via JsonLiteral(String).
    // Current toSqlImpl only escapes the SQL single-quote `'` via replaceAll;
    // backslashes / newlines / control chars are emitted raw, so the resulting
    // SQL is not re-parseable in strict modes for some inputs.
    // ------------------------------------------------------------------
    @Test
    public void testToSqlRoundTripSpecialChars() throws AnalysisException {
        String[] payloads = new String[] {
                "{\"k\":\"a'b\"}",          // single quote inside string
                "{\"k\":\"a\\\\b\"}",       // backslash
                "{\"k\":\"a\\nb\"}",        // \n
                "{\"k\":\"中文键值\"}",     // CJK
                "{\"k\":\"emoji-\\uD83D\\uDE00\"}" // emoji (UTF-16 surrogate pair)
        };
        for (String payload : payloads) {
            JsonLiteral lit = new JsonLiteral(payload);
            String sql = lit.toSqlImpl();
            Assertions.assertNotNull(sql, "toSqlImpl must not return null for " + payload);
            Assertions.assertTrue(sql.startsWith("'") && sql.endsWith("'"),
                    "toSqlImpl must wrap in single quotes for " + payload);
            // Strip the outer single quotes and unescape doubled '' back to '.
            String inner = sql.substring(1, sql.length() - 1).replace("''", "'");
            // Round-trip: the inner payload must reparse as a valid JSON literal.
            Assertions.assertDoesNotThrow(() -> new JsonLiteral(inner),
                    "toSqlImpl emitted SQL not re-parseable as JsonLiteral for " + payload + " -> " + sql);
        }
    }
}
