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

import org.apache.doris.nereids.exceptions.AnalysisException;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.SlotReference;
import org.apache.doris.nereids.trees.expressions.functions.agg.Avg;
import org.apache.doris.nereids.trees.expressions.functions.agg.BitmapUnion;
import org.apache.doris.nereids.trees.expressions.functions.agg.Count;
import org.apache.doris.nereids.trees.expressions.functions.agg.GroupConcat;
import org.apache.doris.nereids.trees.expressions.functions.agg.HllUnion;
import org.apache.doris.nereids.trees.expressions.functions.agg.Max;
import org.apache.doris.nereids.trees.expressions.functions.agg.Min;
import org.apache.doris.nereids.trees.expressions.functions.agg.MultiDistinctCount;
import org.apache.doris.nereids.trees.expressions.functions.agg.Sum;
import org.apache.doris.nereids.trees.expressions.functions.scalar.ToBitmap;
import org.apache.doris.nereids.trees.expressions.literal.BigIntLiteral;
import org.apache.doris.nereids.trees.expressions.literal.DecimalV3Literal;
import org.apache.doris.nereids.trees.expressions.literal.DoubleLiteral;
import org.apache.doris.nereids.trees.expressions.literal.FloatLiteral;
import org.apache.doris.nereids.trees.expressions.literal.IntegerLiteral;
import org.apache.doris.nereids.trees.expressions.literal.LargeIntLiteral;
import org.apache.doris.nereids.trees.expressions.literal.NullLiteral;
import org.apache.doris.nereids.trees.expressions.literal.SmallIntLiteral;
import org.apache.doris.nereids.trees.expressions.literal.StringLiteral;
import org.apache.doris.nereids.trees.expressions.literal.TinyIntLiteral;
import org.apache.doris.nereids.types.ArrayType;
import org.apache.doris.nereids.types.BigIntType;
import org.apache.doris.nereids.types.BitmapType;
import org.apache.doris.nereids.types.DataType;
import org.apache.doris.nereids.types.DecimalV3Type;
import org.apache.doris.nereids.types.DoubleType;
import org.apache.doris.nereids.types.HllType;
import org.apache.doris.nereids.types.IntegerType;
import org.apache.doris.nereids.types.LargeIntType;
import org.apache.doris.nereids.types.MapType;
import org.apache.doris.nereids.types.StringType;
import org.apache.doris.nereids.types.StructType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Feature review tests for the agg_function surface (branch-4.1 SOP pilot).
 *
 * <p>Authored from empirical findings of the agg_function SOP pilot — see
 * {@code regression-test/suites/feature_review_repro/agg_function/} for the FT counterparts.
 * Split into three buckets:
 *
 * <ol>
 *   <li><b>SEV reproductions</b> — currently expected to FAIL on branch-4.1. They assert the
 *       <em>desired</em> (post-fix) behavior, so they flip green once the FE fix lands. Marked
 *       SEV-XYZ-* in javadoc and the test name starts with {@code testSev*}.</li>
 *   <li><b>Type-promotion regression tests</b> — should PASS today. They lock in current
 *       widening behavior for SUM/AVG/COUNT so future refactors don't silently regress it.</li>
 *   <li><b>Aggregate validation tests</b> — should PASS today. They lock in current FE rejection
 *       behavior for invalid arg types (e.g. MIN/MAX on object/complex types, COUNT(DISTINCT
 *       complex)).</li>
 * </ol>
 *
 * <p>All assertions are hard — every test either expects an exception or asserts a specific
 * value. No "log and pass" tests.
 */
public class AggFunctionFeatureReviewTest {

    // ---------- helpers ------------------------------------------------------------------

    /**
     * Mirrors the helper in {@code GetDataTypeTest}: run the legality hooks then return the
     * inferred return type. Anything thrown propagates so callers can assert on exceptions.
     */
    private DataType checkAndGetDataType(Expression expression) {
        expression.checkLegalityBeforeTypeCoercion();
        expression.checkLegalityAfterRewrite();
        return expression.getDataType();
    }

    private SlotReference slot(String name, DataType type) {
        return new SlotReference(name, type);
    }

    // =====================================================================================
    // 1. SEV reproductions — these tests are RED until the FE fix lands.
    // =====================================================================================

    /**
     * SEV (silent overflow): {@code SUM(BIGINT)} should widen to LARGEINT so that summing many
     * large BIGINT values does not silently wrap. Today the signature returns BIGINT — meaning
     * SUM(BIGINT) overflows silently. Probed via FT batch and reproducible at BE.
     *
     * <p>Once the fix lands ({@code Sum.SIGNATURES} promotes BIGINT input to LARGEINT return),
     * this test will go green.
     */
    @Test
    public void testSevSumBigintShouldPromoteToLargeint() {
        Sum sum = new Sum(new BigIntLiteral(1L));
        DataType returnType = checkAndGetDataType(sum);
        Assertions.assertEquals(LargeIntType.INSTANCE, returnType,
                "SEV: SUM(BIGINT) should widen to LARGEINT to prevent silent overflow. "
                        + "Currently returns: " + returnType);
    }

    /**
     * SEV (silent empty bitmap): {@code to_bitmap(-1)} with a known-negative literal should be
     * rejected at FE expression analysis. Today FE accepts it (signature matches BIGINT) and
     * BE silently produces an empty bitmap. The probe confirmed FE returns BITMAP type with no
     * exception.
     *
     * <p>Once FE adds a literal-range check that rejects {@code to_bitmap(<negative literal>)}
     * (via {@code checkLegalityBeforeTypeCoercion} or a new validation hook), this test will
     * go green.
     */
    @Test
    public void testSevToBitmapNegativeLiteralShouldReject() {
        ToBitmap call = new ToBitmap(new IntegerLiteral(-1));
        Assertions.assertThrows(Exception.class,
                () -> {
                    call.checkLegalityBeforeTypeCoercion();
                    call.checkLegalityAfterRewrite();
                    // also force a getDataType so any deferred validation fires
                    call.getDataType();
                },
                "SEV: to_bitmap(<negative literal>) should be rejected at FE; "
                        + "today it silently returns an empty bitmap at runtime");
    }

    // =====================================================================================
    // 2. Type-promotion regression tests — should PASS today and stay green.
    //    These lock in the current widening behavior for SUM/AVG/COUNT.
    // =====================================================================================

    @Test
    public void testSumTinyintReturnsBigint() {
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Sum(new TinyIntLiteral((byte) 1))));
    }

    @Test
    public void testSumSmallintReturnsBigint() {
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Sum(new SmallIntLiteral((short) 1))));
    }

    @Test
    public void testSumIntegerReturnsBigint() {
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Sum(new IntegerLiteral(1))));
    }

    @Test
    public void testSumLargeintReturnsLargeint() {
        Assertions.assertEquals(LargeIntType.INSTANCE,
                checkAndGetDataType(new Sum(new LargeIntLiteral(BigInteger.ONE))));
    }

    @Test
    public void testSumFloatReturnsDouble() {
        // FLOAT does not have a literal class; route through a slot.
        Assertions.assertEquals(DoubleType.INSTANCE,
                checkAndGetDataType(new Sum(new FloatLiteral(1.0F))));
    }

    @Test
    public void testSumDoubleReturnsDouble() {
        Assertions.assertEquals(DoubleType.INSTANCE,
                checkAndGetDataType(new Sum(new DoubleLiteral(1.0))));
    }

    @Test
    public void testSumDecimalWidensToMaxPrecision() {
        // SUM over a Decimal(P,S) must widen precision to MAX_DECIMAL128_PRECISION (=38) with
        // the same scale, otherwise grouped sums of mid-precision decimals can overflow at BE.
        DecimalV3Literal lit = new DecimalV3Literal(new BigDecimal("123.123456"));
        DataType result = checkAndGetDataType(new Sum(lit));
        Assertions.assertTrue(result instanceof DecimalV3Type,
                "Expected DecimalV3Type, got: " + result);
        DecimalV3Type dec = (DecimalV3Type) result;
        Assertions.assertEquals(38, dec.getPrecision(),
                "SUM(Decimal) must widen precision to 38");
        Assertions.assertEquals(6, dec.getScale(),
                "SUM(Decimal) must preserve input scale");
    }

    @Test
    public void testAvgIntegerReturnsDouble() {
        Assertions.assertEquals(DoubleType.INSTANCE,
                checkAndGetDataType(new Avg(new IntegerLiteral(1))));
    }

    @Test
    public void testAvgBigintReturnsDouble() {
        // Even though BIGINT exact-arith might want decimal, current spec is DOUBLE.
        Assertions.assertEquals(DoubleType.INSTANCE,
                checkAndGetDataType(new Avg(new BigIntLiteral(1L))));
    }

    // ---------- NULL / unusual input -----------------------------------------------------

    @Test
    public void testSumNullLiteralReturnsBigint() {
        // NULL → analyzer falls back to BIGINT (per Sum.searchSignature special case).
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Sum(NullLiteral.INSTANCE)));
    }

    @Test
    public void testCountNullLiteralReturnsBigint() {
        // count(NULL) is functionally count(*) of NULL — always BIGINT, never NULL.
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Count(NullLiteral.INSTANCE)));
    }

    @Test
    public void testCountStarReturnsBigint() {
        Assertions.assertEquals(BigIntType.INSTANCE,
                checkAndGetDataType(new Count()));
    }

    // =====================================================================================
    // 3. Aggregate-function validation tests — should PASS today.
    //    These lock in current FE rejection behavior for forbidden arg types.
    // =====================================================================================

    @Test
    public void testMaxRejectsBitmap() {
        // BITMAP is an object/metric type; MIN/MAX/ORDER-BY are forbidden.
        Max max = new Max(slot("bm", BitmapType.INSTANCE));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(max),
                "MAX(BITMAP) must be rejected at FE — BITMAP is a metric type without ordering");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("bitmap")
                        || ex.getMessage().toLowerCase().contains("metric"),
                "Error should mention bitmap/metric, got: " + ex.getMessage());
    }

    @Test
    public void testMinRejectsHll() {
        Min min = new Min(slot("h", HllType.INSTANCE));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(min),
                "MIN(HLL) must be rejected — HLL is a metric type without ordering");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("hll")
                        || ex.getMessage().toLowerCase().contains("metric"),
                "Error should mention hll/metric, got: " + ex.getMessage());
    }

    @Test
    public void testMaxRejectsMap() {
        // MAP has no ordering — FE should reject.
        Max max = new Max(slot("m", MapType.SYSTEM_DEFAULT));
        Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(max),
                "MAX(MAP) must be rejected — MAP has no total ordering");
    }

    @Test
    public void testMinRejectsStruct() {
        Min min = new Min(slot("s", StructType.SYSTEM_DEFAULT));
        Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(min),
                "MIN(STRUCT) must be rejected — STRUCT has no total ordering");
    }

    @Test
    public void testCountDistinctRejectsComplexType() {
        // count(distinct <array>) cannot be evaluated by the dictionary path; FE rejects
        // post-rewrite (checkLegalityAfterRewrite).
        Count count = new Count(true, slot("arr", ArrayType.SYSTEM_DEFAULT));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(count),
                "COUNT(DISTINCT array) must be rejected at FE — complex types cannot be hashed");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("count")
                        || ex.getMessage().toLowerCase().contains("distinct"),
                "Error should mention count/distinct, got: " + ex.getMessage());
    }

    @Test
    public void testCountMultipleArgsWithoutDistinctIsRejected() {
        // count(c1, c2) without DISTINCT is invalid (only DISTINCT supports multi-arg count).
        Count count = new Count(slot("a", IntegerType.INSTANCE), slot("b", IntegerType.INSTANCE));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> checkAndGetDataType(count),
                "COUNT(c1, c2) without DISTINCT must be rejected at FE");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("distinct"),
                "Error should reference DISTINCT, got: " + ex.getMessage());
    }

    // ---------- bitmap_union / hll_union must have matching argument type ---------------

    @Test
    public void testBitmapUnionAcceptsBitmap() {
        // Sanity: BITMAP_UNION on a BITMAP slot is well-typed and returns BITMAP.
        BitmapUnion bu = new BitmapUnion(slot("b", BitmapType.INSTANCE));
        Assertions.assertEquals(BitmapType.INSTANCE, checkAndGetDataType(bu));
    }

    @Test
    public void testHllUnionAcceptsHll() {
        HllUnion hu = new HllUnion(slot("h", HllType.INSTANCE));
        Assertions.assertEquals(HllType.INSTANCE, checkAndGetDataType(hu));
    }

    // ---------- GROUP_CONCAT spec --------------------------------------------------------

    @Test
    public void testGroupConcatVarcharIsWellTyped() {
        // group_concat over a varchar slot returns varchar — sanity check.
        GroupConcat gc = new GroupConcat(slot("s", StringType.INSTANCE));
        DataType ret = checkAndGetDataType(gc);
        // Implementation returns VarcharType.SYSTEM_DEFAULT; assert string-family.
        Assertions.assertTrue(ret.isStringLikeType(),
                "group_concat should return a string-family type, got: " + ret);
    }

    // ---------- MultiDistinctCount sanity -------------------------------------------------

    @Test
    public void testMultiDistinctCountReturnsBigint() {
        MultiDistinctCount mdc = new MultiDistinctCount(slot("a", IntegerType.INSTANCE));
        Assertions.assertEquals(BigIntType.INSTANCE, checkAndGetDataType(mdc));
    }

    @Test
    public void testToBitmapPositiveLiteralAccepted() {
        // Positive literal — sanity that the to_bitmap path is otherwise well-typed and we
        // are only asserting the negative-literal case in the SEV test.
        ToBitmap call = new ToBitmap(new BigIntLiteral(42L));
        Assertions.assertEquals(BitmapType.INSTANCE, checkAndGetDataType(call));
    }

    // ---------- COUNT(DISTINCT) on a literal must reject (regression lock) --------------

    @Test
    public void testCountWithoutDistinctRejectsZeroArgs() {
        // count() with zero non-* args and distinct=true is invalid.
        Assertions.assertThrows(AnalysisException.class,
                () -> new Count().withDistinctAndChildren(true, java.util.Collections.emptyList()),
                "Can not count distinct empty arguments — must throw");
    }

    @Test
    public void testStringLiteralForGroupConcatStillStringTyped() {
        // group_concat(<string literal>) is still well-typed in FE (BE rejects all-literal
        // aggregation later); FE should not break here.
        GroupConcat gc = new GroupConcat(new StringLiteral("a"));
        DataType ret = checkAndGetDataType(gc);
        Assertions.assertTrue(ret.isStringLikeType(),
                "group_concat(<literal string>) should still be string-typed, got: " + ret);
    }
}
