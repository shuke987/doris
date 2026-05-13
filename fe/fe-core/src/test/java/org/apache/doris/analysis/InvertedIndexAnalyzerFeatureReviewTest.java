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

import org.apache.doris.catalog.AggregateType;
import org.apache.doris.catalog.Index;
import org.apache.doris.catalog.KeysType;
import org.apache.doris.catalog.PrimitiveType;
import org.apache.doris.common.AnalysisException;
import org.apache.doris.nereids.trees.plans.commands.info.ColumnDefinition;
import org.apache.doris.nereids.trees.plans.commands.info.IndexDefinition;
import org.apache.doris.nereids.types.ArrayType;
import org.apache.doris.nereids.types.DateV2Type;
import org.apache.doris.nereids.types.DecimalV3Type;
import org.apache.doris.nereids.types.IntegerType;
import org.apache.doris.nereids.types.MapType;
import org.apache.doris.nereids.types.StringType;
import org.apache.doris.nereids.types.StructField;
import org.apache.doris.nereids.types.StructType;
import org.apache.doris.nereids.types.VariantType;
import org.apache.doris.thrift.TInvertedIndexFileStorageFormat;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Feature review tests for the inverted_index_analyzer surface (branch-4.1 pilot).
 *
 * <p>These tests were authored from empirical findings of the inverted_index_analyzer SOP pilot:
 * see {@code regression-test/suites/feature_review_repro/inverted_index_analyzer/}. They split
 * into three buckets:
 *
 * <ol>
 *   <li><b>SEV-2 reproductions</b> — currently expected to FAIL on branch-4.1 because FE does
 *       not validate {@code char_filter_replacement} length / emptiness. These tests assert
 *       the desired (post-fix) behavior so they will turn green once the FE fix lands.</li>
 *   <li><b>Validation regression tests</b> — should PASS today. They lock in the existing
 *       (correct) FE validation behavior that the pilot already confirmed working.</li>
 *   <li><b>Default-injection tests</b> — assert that {@code support_phrase=true} and
 *       {@code lower_case=true} are auto-injected by {@link Index} when (and only when) an
 *       analyzer / parser / normalizer is configured.</li>
 * </ol>
 *
 * <p>All assertions are hard — every test either expects an exception or asserts a specific
 * value. No "log and pass" tests.
 */
public class InvertedIndexAnalyzerFeatureReviewTest {

    // ---------- helpers ------------------------------------------------------------------

    private Map<String, String> props(String... kv) {
        if (kv.length % 2 != 0) {
            throw new IllegalArgumentException("props() requires an even number of arguments");
        }
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1]);
        }
        return m;
    }

    private void checkProps(Map<String, String> properties) throws AnalysisException {
        InvertedIndexUtil.checkInvertedIndexProperties(
                properties, PrimitiveType.STRING, TInvertedIndexFileStorageFormat.V2);
    }

    // =====================================================================================
    // 1. Confirmed SEV-2 reproductions
    //    These tests will FAIL on current branch-4.1 (FE missing validation) — that is the
    //    intended signal. Once FE adds the length==1 / non-empty check they will go green.
    // =====================================================================================

    /**
     * SEV-2: {@code char_filter_replacement} must be exactly one ASCII character. Currently
     * FE accepts multi-char strings and BE silently truncates to the first byte (verified
     * via {@code tokenize()} on cluster).
     */
    @Test
    public void testCharFilterReplacementMustBeSingleChar() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, ".",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, "xyz");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkProps(p),
                "SEV-2: FE should reject multi-char char_filter_replacement (currently silently truncated by BE)");
        Assertions.assertTrue(
                ex.getMessage().toLowerCase().contains("char_filter_replacement"),
                "Error message should mention char_filter_replacement, got: " + ex.getMessage());
    }

    /**
     * SEV-2: {@code char_filter_replacement} cannot be the empty string. Currently FE
     * accepts and BE injects a NUL byte into the tokenized output.
     */
    @Test
    public void testCharFilterReplacementCannotBeEmpty() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, ".",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, "");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkProps(p),
                "SEV-2: FE should reject empty char_filter_replacement (currently BE injects \\0 byte)");
        Assertions.assertTrue(
                ex.getMessage().toLowerCase().contains("char_filter_replacement"),
                "Error message should mention char_filter_replacement, got: " + ex.getMessage());
    }

    // =====================================================================================
    // 2. Validation regression tests — should PASS today
    // =====================================================================================

    @Test
    public void testParserMustBeLowercase() {
        // Parser value is matched case-sensitively against the allow-list regex.
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkProps(props(InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "English")));
        Assertions.assertTrue(ex.getMessage().contains("parser"),
                "Error message should reference parser, got: " + ex.getMessage());
    }

    @Test
    public void testParserModeOnlyForChineseAndIk() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_MODE_KEY, "fine_grained");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        String msg = ex.getMessage().toLowerCase();
        Assertions.assertTrue(msg.contains("chinese") && msg.contains("ik"),
                "Error message should mention both chinese and ik, got: " + ex.getMessage());
    }

    @Test
    public void testParserModeInvalidValue() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "chinese",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_MODE_KEY, "ultra_fine");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("parser_mode"),
                "Error message should reference parser_mode, got: " + ex.getMessage());
    }

    @Test
    public void testCharFilterPatternMustBeAscii() {
        // em-dash (U+2014) is NOT ASCII
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, "—",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, " ");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("ASCII"),
                "Error message should reference ASCII, got: " + ex.getMessage());
    }

    @Test
    public void testCharFilterReplacementMustBeAscii() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, ".",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, "—");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("ASCII"),
                "Error message should reference ASCII, got: " + ex.getMessage());
    }

    @Test
    public void testCharFilterMissingPattern() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, " ");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("char_filter_pattern"),
                "Error message should mention char_filter_pattern, got: " + ex.getMessage());
    }

    /**
     * When {@code char_filter_replacement} is omitted entirely, the current FE accepts it
     * (BE will default replacement to " "). This locks in that contract — if FE ever
     * starts requiring it, this test will fail and force a deliberate review.
     */
    @Test
    public void testCharFilterMissingReplacementAccepted() throws AnalysisException {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "char_replace",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, ".");
        checkProps(p); // Must NOT throw
        Assertions.assertEquals("char_replace",
                p.get(InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE));
    }

    @Test
    public void testCharFilterUnknownType() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, "fake_filter_type");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("char_filter_type"),
                "Error message should mention char_filter_type, got: " + ex.getMessage());
    }

    @Test
    public void testStopwordsMustBeNone() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_STOPWORDS_KEY, "english");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("stopWords must be none"),
                "Error message should say 'stopWords must be none', got: " + ex.getMessage());
    }

    @Test
    public void testIgnoreAboveMustBePositive() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_IGNORE_ABOVE_KEY, "0");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("ignore_above must be positive"),
                "Error message should say 'ignore_above must be positive', got: " + ex.getMessage());
    }

    @Test
    public void testLowerCaseMustBeLowercaseBool() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_PARSER_LOWERCASE_KEY, "True");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("lower_case must be true or false"),
                "Error message should say 'lower_case must be true or false', got: " + ex.getMessage());
    }

    @Test
    public void testParserAndAnalyzerMutuallyExclusive() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                InvertedIndexUtil.INVERTED_INDEX_ANALYZER_NAME_KEY, "my_analyzer");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("Cannot specify more than one"),
                "Error message should say 'Cannot specify more than one ...', got: " + ex.getMessage());
    }

    @Test
    public void testUnknownPropertyRejected() {
        Map<String, String> p = props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english",
                "totally_unrelated_key", "whatever");
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class, () -> checkProps(p));
        Assertions.assertTrue(ex.getMessage().contains("Invalid inverted index property key"),
                "Error message should say 'Invalid inverted index property key', got: " + ex.getMessage());
    }

    // ----- column-type validation (Nereids IndexDefinition path) --------------------------

    private IndexDefinition invertedIndexDef(Map<String, String> properties) {
        return new IndexDefinition("idx_ft", false, Lists.newArrayList("c"), "INVERTED",
                properties, "fr");
    }

    private ColumnDefinition col(String name,
            org.apache.doris.nereids.types.DataType type) {
        return new ColumnDefinition(name, type, false, AggregateType.NONE, true, null, "c");
    }

    @Test
    public void testParserOnNonStringColumnInteger() {
        IndexDefinition def = invertedIndexDef(props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english"));
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", IntegerType.INSTANCE),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(ex.getMessage().contains("not supported")
                        || ex.getMessage().contains("invalid"),
                "Error should reject INT + parser, got: " + ex.getMessage());
    }

    @Test
    public void testParserOnNonStringColumnDate() {
        IndexDefinition def = invertedIndexDef(props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english"));
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", DateV2Type.INSTANCE),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(ex.getMessage().contains("not supported")
                        || ex.getMessage().contains("invalid"),
                "Error should reject DATE + parser, got: " + ex.getMessage());
    }

    @Test
    public void testParserOnNonStringColumnDecimal() {
        IndexDefinition def = invertedIndexDef(props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english"));
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", DecimalV3Type.createDecimalV3Type(18, 4)),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(ex.getMessage().contains("not supported")
                        || ex.getMessage().contains("invalid"),
                "Error should reject DECIMAL + parser, got: " + ex.getMessage());
    }

    @Test
    public void testInvertedIndexNotAllowedOnMap() {
        IndexDefinition def = invertedIndexDef(new HashMap<>());
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", MapType.of(StringType.INSTANCE, IntegerType.INSTANCE)),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(ex.getMessage().contains("not supported"),
                "Error should reject MAP, got: " + ex.getMessage());
    }

    /**
     * Variant rejection on V1 format (the existing FE guardrail for V1 + variant when
     * {@code enable_inverted_index_v1_for_variant=false}, which is the production default).
     */
    @Test
    public void testInvertedIndexNotAllowedOnVariantV1() {
        IndexDefinition def = invertedIndexDef(new HashMap<>());
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", VariantType.INSTANCE),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V1));
        Assertions.assertTrue(
                ex.getMessage().contains("not supported in inverted index format V1"),
                "Error should reject Variant on V1, got: " + ex.getMessage());
    }

    @Test
    public void testInvertedIndexNotAllowedOnStruct() {
        IndexDefinition def = invertedIndexDef(new HashMap<>());
        List<StructField> fields = Arrays.asList(
                new StructField("f1", StringType.INSTANCE, true, ""),
                new StructField("f2", IntegerType.INSTANCE, true, ""));
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", new StructType(fields)),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(ex.getMessage().contains("not supported"),
                "Error should reject STRUCT, got: " + ex.getMessage());
    }

    /**
     * Positive case: ARRAY<STRING> + parser='english' is accepted by FE. Catches any
     * accidental future regression that over-rejects array-of-string with parser.
     *
     * <p>Note: the legacy {@link InvertedIndexUtil#checkInvertedIndexParser} rejects
     * array-with-parser at the {@code PrimitiveType.ARRAY} level. This test exercises the
     * Nereids {@link IndexDefinition#checkColumn} path, which delegates to that same
     * legacy check via {@code colType.toCatalogDataType().getPrimitiveType()}. If 4.x ever
     * relaxes that to allow parsers on ARRAY&lt;STRING&gt; this test will need to be
     * inverted — keep it as a tripwire.
     */
    @Test
    public void testArrayStringWithParserCurrentlyRejected() {
        IndexDefinition def = invertedIndexDef(props(
                InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english"));
        // Today: legacy checkInvertedIndexParser refuses array+parser regardless of element type.
        // This is intentional 4.1 behavior; if the rule changes this test will flip.
        org.apache.doris.nereids.exceptions.AnalysisException ex =
                Assertions.assertThrows(org.apache.doris.nereids.exceptions.AnalysisException.class,
                        () -> def.checkColumn(col("c", ArrayType.of(StringType.INSTANCE)),
                                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2));
        Assertions.assertTrue(
                ex.getMessage().contains("is not supported for array column")
                        || ex.getMessage().contains("not supported"),
                "Error should explain ARRAY+parser rejection, got: " + ex.getMessage());
    }

    /**
     * Positive case: ARRAY&lt;STRING&gt; without parser is accepted (column-type check
     * succeeds — only the "parser on array" rule rejects analyzers, not the array type
     * itself).
     */
    @Test
    public void testArrayStringNoParserAllowed() throws Exception {
        IndexDefinition def = invertedIndexDef(new HashMap<>());
        def.checkColumn(col("c", ArrayType.of(StringType.INSTANCE)),
                KeysType.DUP_KEYS, false, TInvertedIndexFileStorageFormat.V2);
        // No exception => pass. Add a hard assertion that the index type stayed INVERTED.
        Assertions.assertEquals(IndexDef.IndexType.INVERTED, def.getIndexType());
    }

    // =====================================================================================
    // 3. Default injection tests — exercise the constructor side-effects of catalog.Index
    // =====================================================================================

    @Test
    public void testSupportPhraseDefaultsToTrue() {
        Map<String, String> input = props(InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english");
        Index idx = new Index(1L, "i", Lists.newArrayList("c"),
                IndexDef.IndexType.INVERTED, input, "");
        Assertions.assertEquals("true",
                idx.getProperties().get(InvertedIndexUtil.INVERTED_INDEX_SUPPORT_PHRASE_KEY),
                "support_phrase should default to 'true' when parser is set");
    }

    @Test
    public void testLowerCaseDefaultsToTrue() {
        Map<String, String> input = props(InvertedIndexUtil.INVERTED_INDEX_PARSER_KEY, "english");
        Index idx = new Index(2L, "i", Lists.newArrayList("c"),
                IndexDef.IndexType.INVERTED, input, "");
        Assertions.assertEquals("true",
                idx.getProperties().get(InvertedIndexUtil.INVERTED_INDEX_PARSER_LOWERCASE_KEY),
                "lower_case should default to 'true' when parser is set");
    }

    @Test
    public void testNoDefaultsWhenNoParser() {
        // Build an inverted index Index with empty props — no parser/analyzer/normalizer set.
        // Defaults should NOT be injected.
        Map<String, String> input = new HashMap<>();
        Index idx = new Index(3L, "i", Lists.newArrayList("c"),
                IndexDef.IndexType.INVERTED, input, "");
        Assertions.assertFalse(
                idx.getProperties().containsKey(InvertedIndexUtil.INVERTED_INDEX_SUPPORT_PHRASE_KEY),
                "support_phrase should NOT be auto-injected when no analyzer/parser/normalizer");
        Assertions.assertFalse(
                idx.getProperties().containsKey(InvertedIndexUtil.INVERTED_INDEX_PARSER_LOWERCASE_KEY),
                "lower_case should NOT be auto-injected when no analyzer/parser/normalizer");
    }
}
