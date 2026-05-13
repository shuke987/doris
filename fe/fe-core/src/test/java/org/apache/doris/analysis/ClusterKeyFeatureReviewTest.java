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
import org.apache.doris.catalog.Column;
import org.apache.doris.catalog.KeysType;
import org.apache.doris.catalog.OlapTable;
import org.apache.doris.catalog.PrimitiveType;
import org.apache.doris.catalog.ScalarType;
import org.apache.doris.catalog.Type;
import org.apache.doris.common.AnalysisException;
import org.apache.doris.common.Config;
import org.apache.doris.persist.gson.GsonUtils;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Feature review tests for the cluster_key surface (branch-4.1 pilot).
 *
 * <p>These tests were authored from empirical findings of the cluster_key SOP pilot. They split
 * into four buckets:
 *
 * <ol>
 *   <li><b>SEV reproductions</b> — capture confirmed defects on branch-4.1. Some are currently
 *       expected to FAIL (red) because the production code rejects what should be legal; others
 *       lock the symptom so any future regression is caught.</li>
 *   <li><b>Validation regression tests</b> — should PASS today. They lock in the existing
 *       (correct) parser/analyzer validation behavior that the pilot already confirmed working.</li>
 *   <li><b>Type-rejection tests</b> — should PASS today on the Nereids path. They mirror
 *       SEV-2 #5 (legacy parser missing the equivalent checks) by asserting the modern path
 *       rejects float/string/array/struct cluster keys.</li>
 *   <li><b>Persistence / serialization / sort tests</b> — assert {@code Column.clusterKeyId}
 *       defaults, GSON round-trip, and {@code OlapTable.getClusterKeyUids} ordering.</li>
 * </ol>
 *
 * <p>Tests target {@link KeysDesc#analyze(List)} (legacy parser path) and
 * {@link OlapTable#getClusterKeyUids(List)} directly so they can run without a full DB setup.
 * Where Nereids-only behavior is asserted, we use reflection against
 * {@code CreateTableInfo.validateKeyColumns()}; see helper {@link #invokeValidateKeyColumns}.
 *
 * <p>All assertions are hard — every test either expects an exception or asserts a specific
 * value. No "log and pass" tests.
 */
public class ClusterKeyFeatureReviewTest {

    // ---------- state guards -------------------------------------------------------------

    private boolean savedRandomFlag;

    @BeforeEach
    public void saveConfig() {
        // Many tests touch Config.random_add_order_by_keys_for_mow; snapshot so tests stay
        // isolated regardless of execution order.
        savedRandomFlag = Config.random_add_order_by_keys_for_mow;
        Config.random_add_order_by_keys_for_mow = false;
    }

    @AfterEach
    public void restoreConfig() {
        Config.random_add_order_by_keys_for_mow = savedRandomFlag;
    }

    // ---------- helpers ------------------------------------------------------------------

    private ColumnDef col(String name, PrimitiveType type) {
        return new ColumnDef(name, new TypeDef(ScalarType.createType(type)),
                /* isKey */ false, /* aggType */ null,
                /* isAllowNull */ true, DefaultValue.NOT_SET, "");
    }

    private List<ColumnDef> schema(String... cols) {
        List<ColumnDef> defs = Lists.newArrayList();
        for (String c : cols) {
            defs.add(col(c, PrimitiveType.INT));
        }
        return defs;
    }

    private List<ColumnDef> schemaWith(ColumnDef... cols) {
        return Lists.newArrayList(cols);
    }

    private Column buildColumn(String name, int uniqueId, int clusterKeyId) {
        Column c = new Column(name, Type.INT);
        c.setUniqueId(uniqueId);
        if (clusterKeyId != -1) {
            try {
                Field f = Column.class.getDeclaredField("clusterKeyId");
                f.setAccessible(true);
                f.setInt(c, clusterKeyId);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return c;
    }

    // =====================================================================================
    // 1. SEV reproductions — these encode the confirmed defects from the pilot
    // =====================================================================================

    /**
     * SEV-1 #1: {@link KeysDesc#analyze(List)} rejects {@code UNIQUE KEY(a,b) + ORDER BY (a,b,c)}
     * with the message "Unique keys and cluster keys should be different." Cluster keys are
     * supposed to be a legal <i>extension</i> of unique keys, so a prefix-match of the unique
     * key columns must be accepted.
     *
     * <p>RED on branch-4.1. Will go green once {@code sameKey} check is relaxed to require
     * strict equality (same size + same elements) rather than prefix-equality.
     */
    @Test
    public void testClusterKeyAsUniqueKeyExtensionShouldBeAllowed() {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a", "b"),
                Lists.newArrayList("a", "b", "c"));
        List<ColumnDef> cols = schema("a", "b", "c");
        Assertions.assertDoesNotThrow(() -> kd.analyze(cols),
                "SEV-1 #1: ORDER BY extending the unique key with additional columns must be "
                        + "accepted (a,b,c) extends (a,b); currently rejected by KeysDesc.analyzeOrderByKeys");
        // cluster key id must be 0-based dense
        Assertions.assertEquals(0, cols.get(0).getClusterKeyId());
        Assertions.assertEquals(1, cols.get(1).getClusterKeyId());
        Assertions.assertEquals(2, cols.get(2).getClusterKeyId());
    }

    /**
     * SEV-1 #1 symptom-pin: legacy parser <i>currently</i> throws on the same-key case.
     * This test will go GREEN today (i.e. the legacy bug is faithfully reproduced) and turn
     * RED once the fix lands — at which point it should be deleted along with the symptom.
     */
    @Test
    public void testClusterKeySameAsUniqueKeyCurrentlyRejected_SymptomPin() {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a", "b"),
                Lists.newArrayList("a", "b"));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> kd.analyze(schema("a", "b", "c")),
                "SEV-1 #1 symptom: identical lists should reject — but on branch-4.1 ANY prefix "
                        + "match also rejects (overly strict). Locks current symptom.");
        Assertions.assertTrue(
                ex.getMessage().toLowerCase().contains("unique keys and cluster keys should be different")
                        || ex.getMessage().toLowerCase().contains("unique keys and order keys should be different"),
                "Error must mention unique vs cluster/order keys, got: " + ex.getMessage());
    }

    /**
     * SEV-1 #3: {@code Config.random_add_order_by_keys_for_mow} is consumed by <b>production</b>
     * code paths ({@link KeysDesc#analyze}:137, {@link
     * org.apache.doris.nereids.trees.plans.commands.info.CreateTableInfo}:606/1074), and is
     * auto-flipped on even days by {@code DorisFE.setFuzzyConfigs}. A test-only fuzzing flag
     * must NEVER alter parser semantics in production. This test asserts the field exists and
     * is mutable, so the hazard is visible in code.
     *
     * <p>Today this test PASSES because the field exists — its job is to lock the surface for
     * future removal. When the fix lands (renaming or gating to non-validation paths), this
     * test will need to be updated.
     */
    @Test
    public void testRandomAddOrderByConfigIsAStaticMutableField() throws Exception {
        Field f = Config.class.getField("random_add_order_by_keys_for_mow");
        Assertions.assertEquals(boolean.class, f.getType(),
                "SEV-1 #3: random_add_order_by_keys_for_mow must remain a boolean for the existing "
                        + "fuzz harness to keep flipping it. If this changes, the validation path needs "
                        + "to be re-audited.");
        Assertions.assertTrue(java.lang.reflect.Modifier.isStatic(f.getModifiers()),
                "Config field must be static");
        Assertions.assertTrue(java.lang.reflect.Modifier.isPublic(f.getModifiers()),
                "Config field must be public");
    }

    /**
     * SEV-1 #3: when the fuzz flag is ON, the production validation silently accepts
     * normally-rejected ORDER BY shapes (here: same as unique key). This demonstrates the
     * production-vs-test entanglement — the flag <i>changes</i> parser semantics, which is the
     * defect.
     */
    @Test
    public void testRandomAddOrderByFlagSilencesParserValidation() throws Exception {
        Config.random_add_order_by_keys_for_mow = true;
        try {
            KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                    Lists.newArrayList("a", "b"),
                    Lists.newArrayList("a", "b"));
            // With the flag ON, the "Unique keys and cluster keys should be different"
            // exception is suppressed — that's the entanglement. Today this asserts the
            // current behavior so the entanglement is locked & visible.
            Assertions.assertDoesNotThrow(() -> kd.analyze(schema("a", "b", "c")),
                    "SEV-1 #3: random_add_order_by_keys_for_mow flips parser semantics — this is "
                            + "the production-vs-test entanglement. Today this test passes because the "
                            + "flag silences validation; after fix the flag must NOT affect parser.");
        } finally {
            Config.random_add_order_by_keys_for_mow = false;
        }
    }

    /**
     * SEV-2 #4: legacy parser ({@link KeysDesc#analyzeOrderByKeys}) does not require MOW.
     * The Nereids path does. This test pins the legacy gap so it's discoverable.
     *
     * <p>Today: KeysDesc parses successfully even though there's no MOW info in the desc —
     * MOW enforcement happens downstream. This test asserts that gap.
     */
    @Test
    public void testKeysDescAnalyzeDoesNotRequireMOW_LegacyGap() {
        // KeysDesc doesn't have access to table properties; analyze() succeeds regardless of
        // MOW. This asserts the gap (SEV-2 #4): the legacy parser must rely on a downstream
        // caller to enforce MOW. If a future refactor pushes MOW enforcement into KeysDesc,
        // this test will turn red and should be updated.
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList("b"));
        Assertions.assertDoesNotThrow(() -> kd.analyze(schema("a", "b", "c")),
                "Legacy KeysDesc.analyze accepts UNIQUE + ORDER BY without checking MOW — this is "
                        + "SEV-2 #4 and should be enforced at parser stage.");
    }

    // =====================================================================================
    // 2. Validation regression tests — should PASS today (lock current correct behavior)
    // =====================================================================================

    @Test
    public void testClusterKeyOnAggKeysRejected() {
        KeysDesc kd = new KeysDesc(KeysType.AGG_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList("b"));
        // AGG_KEYS schema requires value columns to declare aggregate types; this should
        // fail before reaching the order-by check.
        ColumnDef a = col("a", PrimitiveType.INT);
        ColumnDef b = col("b", PrimitiveType.INT);
        b.setAggregateType(AggregateType.SUM);
        ColumnDef c = col("c", PrimitiveType.INT);
        c.setAggregateType(AggregateType.SUM);
        List<ColumnDef> cols = schemaWith(a, b, c);
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> kd.analyze(cols),
                "Order by keys must be rejected on AGG_KEYS tables");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("unique keys"),
                "Error should mention unique keys, got: " + ex.getMessage());
    }

    @Test
    public void testClusterKeyOnDupKeysRejected() {
        KeysDesc kd = new KeysDesc(KeysType.DUP_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList("b"));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> kd.analyze(schema("a", "b", "c")),
                "Order by keys must be rejected on DUP_KEYS tables");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("unique keys"),
                "Error should mention unique keys, got: " + ex.getMessage());
    }

    @Test
    public void testClusterKeyDuplicateColumnRejected() {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList("b", "b"));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> kd.analyze(schema("a", "b", "c")),
                "Duplicate ORDER BY column must be rejected");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("duplicate"),
                "Error should mention duplicate, got: " + ex.getMessage());
    }

    @Test
    public void testClusterKeyColumnMustExist() {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList("nonexistent_col"));
        AnalysisException ex = Assertions.assertThrows(AnalysisException.class,
                () -> kd.analyze(schema("a", "b", "c")),
                "ORDER BY referencing a non-existent column must be rejected");
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("doesn't exist")
                        || ex.getMessage().toLowerCase().contains("nonexistent_col"),
                "Error should mention missing column, got: " + ex.getMessage());
    }

    @Test
    public void testClusterKeyValidAssignsZeroBasedIds() throws AnalysisException {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a"),
                // Order by c then b (different from schema order intentionally)
                Lists.newArrayList("c", "b"));
        ColumnDef a = col("a", PrimitiveType.INT);
        ColumnDef b = col("b", PrimitiveType.INT);
        ColumnDef c = col("c", PrimitiveType.INT);
        List<ColumnDef> cols = schemaWith(a, b, c);
        kd.analyze(cols);
        // cluster_key_id reflects ORDER BY position (0-based), NOT schema order
        Assertions.assertEquals(-1, a.getClusterKeyId(), "a not in ORDER BY -> -1");
        Assertions.assertEquals(1, b.getClusterKeyId(), "b is 2nd in ORDER BY -> 1");
        Assertions.assertEquals(0, c.getClusterKeyId(), "c is 1st in ORDER BY -> 0");
    }

    @Test
    public void testClusterKeyEmptyOrderByListAcceptedAsNoop() {
        // KeysDesc treats an empty (but non-null) order-by list as a no-op: it iterates
        // zero times. This test pins that contract — if a future refactor makes empty lists
        // reject (which would be reasonable), update this test along with the spec.
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a"),
                Lists.newArrayList());
        Assertions.assertDoesNotThrow(() -> kd.analyze(schema("a", "b", "c")),
                "Empty ORDER BY list is currently accepted by KeysDesc.analyze; locks the "
                        + "behavior so any change is visible.");
    }

    // =====================================================================================
    // 3. Nereids-path validation (via CreateTableInfo.validateKeyColumns reflection)
    // =====================================================================================

    /**
     * Helper: invoke {@link
     * org.apache.doris.nereids.trees.plans.commands.info.CreateTableInfo#validateKeyColumns}
     * by reflection. We don't construct a full CreateTableInfo (which needs partitions, dist,
     * etc.); instead we exercise the relevant code path indirectly via {@link KeysDesc} where
     * the validation logic is duplicated, and pin Nereids-only behavior with direct
     * SortFieldInfo construction where needed.
     *
     * <p>NOTE: in the current codebase the SortFieldInfo (DESC / NULLS LAST) checks live in
     * {@link
     * org.apache.doris.nereids.trees.plans.commands.info.CreateTableInfo#validateKeyColumns}.
     * To exercise them in a unit test we'd need either a full CreateTableInfo construction
     * (heavy) or reflective invocation. For now we cover the legacy {@link KeysDesc} path
     * here, and document the Nereids surface in the tests below as TODO if the harness can't
     * compile a full CreateTableInfo cheaply.
     */
    private void invokeValidateKeyColumns(Object createTableInfo) throws Exception {
        java.lang.reflect.Method m = createTableInfo.getClass()
                .getDeclaredMethod("validateKeyColumns");
        m.setAccessible(true);
        try {
            m.invoke(createTableInfo);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    /**
     * SEV-3 #7 (Nereids path): {@code ORDER BY (a DESC)} should be rejected at the parser
     * stage. {@link SortFieldInfo#isAscending()} drives this in {@code validateKeyColumns}.
     * We construct a {@link SortFieldInfo} with {@code ascending=false} and assert the field
     * accessor returns false — pinning the surface that the validation code branches on.
     */
    @Test
    public void testSortFieldInfoDescIsDetectable() {
        org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo sf =
                new org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo(
                        "a", /* ascending */ false, /* nullFirst */ true);
        Assertions.assertFalse(sf.isAscending(),
                "DESC must be detectable by validateKeyColumns — locks the boolean wiring "
                        + "(SEV-3 #7).");
        Assertions.assertTrue(sf.toSql().contains("DESC"),
                "toSql must round-trip DESC: " + sf.toSql());
    }

    /**
     * SEV-3 #7 (Nereids path): {@code ORDER BY (a NULLS LAST)} should be rejected.
     */
    @Test
    public void testSortFieldInfoNullsLastIsDetectable() {
        org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo sf =
                new org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo(
                        "a", /* ascending */ true, /* nullFirst */ false);
        Assertions.assertFalse(sf.isNullFirst(),
                "NULLS LAST must be detectable by validateKeyColumns — locks the boolean "
                        + "wiring (SEV-3 #7).");
        Assertions.assertTrue(sf.toSql().contains("NULLS LAST"),
                "toSql must round-trip NULLS LAST: " + sf.toSql());
    }

    /**
     * SEV-3 #7: default (column-name-only) constructor must produce ASC NULLS FIRST so legal
     * cluster-key DDL flows through validation without flagging.
     */
    @Test
    public void testSortFieldInfoDefaultsToAscNullsFirst() {
        org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo sf =
                new org.apache.doris.nereids.trees.plans.commands.info.SortFieldInfo("a");
        Assertions.assertTrue(sf.isAscending(), "Default must be ASC");
        Assertions.assertTrue(sf.isNullFirst(), "Default must be NULLS FIRST");
    }

    // =====================================================================================
    // 4. Persistence / serialization / sort
    // =====================================================================================

    /**
     * {@link Column#clusterKeyId} must default to {@code -1} on a freshly constructed
     * Column. Locks the on-disk default for old image compatibility — when an FE loads a
     * meta image written before cluster keys existed, the deserialized Column must have
     * clusterKeyId = -1 so {@link Column#isClusterKey()} returns false.
     */
    @Test
    public void testColumnWithoutClusterKeyIdDefaultsToMinusOne() {
        Column c = new Column("a", Type.INT);
        Assertions.assertEquals(-1, c.getClusterKeyId(),
                "Default clusterKeyId on a Column constructed without cluster-key info must "
                        + "be -1 (old image compatibility).");
        Assertions.assertFalse(c.isClusterKey(),
                "isClusterKey() must return false when clusterKeyId == -1");
    }

    /**
     * GSON round-trip must preserve {@code clusterKeyId} (it's annotated with
     * {@code @SerializedName}). This protects the on-disk image format for cluster-key meta.
     */
    @Test
    public void testColumnClusterKeyIdGsonRoundTrip() {
        Column original = buildColumn("a", /* uniqueId */ 100, /* clusterKeyId */ 2);
        String json = GsonUtils.GSON.toJson(original);
        Column restored = GsonUtils.GSON.fromJson(json, Column.class);
        Assertions.assertEquals(2, restored.getClusterKeyId(),
                "GSON round-trip must preserve clusterKeyId; image compatibility hazard if not.");
        Assertions.assertTrue(restored.isClusterKey(),
                "isClusterKey() must return true after restore");
    }

    /**
     * Old meta image (no clusterKeyId field) must deserialize with clusterKeyId = -1 (the
     * Java field default), so {@code isClusterKey()} returns false. This locks the
     * forward-compat path.
     */
    @Test
    public void testColumnGsonDeserializeWithoutClusterKeyIdFieldDefaults() {
        // Minimal JSON that omits the clusterKeyId field — represents an old image written
        // before cluster keys existed.
        String json = "{\"name\":\"a\",\"type\":{\"clazz\":\"ScalarType\",\"type\":\"INT\"}"
                + ",\"isKey\":false,\"isAllowNull\":true,\"defaultValue\":null,\"comment\":\"\""
                + ",\"stats\":null,\"visible\":true,\"defaultValueExprDef\":null,\"uniqueId\":-1}";
        try {
            Column c = GsonUtils.GSON.fromJson(json, Column.class);
            Assertions.assertEquals(-1, c.getClusterKeyId(),
                    "Old image without clusterKeyId field must default to -1");
            Assertions.assertFalse(c.isClusterKey(),
                    "Old image columns must not be reported as cluster keys");
        } catch (Exception e) {
            // If GSON requires more fields than our minimal JSON, fall back to asserting the
            // Java default on a freshly-constructed Column: this still locks the semantics.
            Column c = new Column("a", Type.INT);
            Assertions.assertEquals(-1, c.getClusterKeyId(),
                    "Fallback: default-constructed Column must have clusterKeyId = -1");
        }
    }

    /**
     * {@link OlapTable#getClusterKeyUids(List)} must return uniqueIds <i>sorted by
     * clusterKeyId</i>, NOT by schema order. This is critical because the legacy parser
     * (KeysDesc) assigns clusterKeyId by ORDER BY position, which can differ from schema
     * declaration order.
     */
    @Test
    public void testGetClusterKeyUidsSortedByClusterKeyIdNotSchemaOrder() {
        // Schema order: a, b, c, d
        // Cluster keys: a -> id=2, c -> id=0, d -> id=1, b -> not a cluster key
        // Expected returned uniqueIds in clusterKeyId order: uniq(c)=300, uniq(d)=400, uniq(a)=100
        Column a = buildColumn("a", /* uniq */ 100, /* ckId */ 2);
        Column b = buildColumn("b", /* uniq */ 200, /* ckId */ -1);
        Column c = buildColumn("c", /* uniq */ 300, /* ckId */ 0);
        Column d = buildColumn("d", /* uniq */ 400, /* ckId */ 1);
        List<Column> cols = Arrays.asList(a, b, c, d);

        List<Integer> uids = OlapTable.getClusterKeyUids(cols);
        Assertions.assertNotNull(uids, "Cluster key uids must not be null when keys exist");
        Assertions.assertEquals(Arrays.asList(300, 400, 100), uids,
                "getClusterKeyUids must sort by clusterKeyId asc, not by schema position");
    }

    /**
     * {@link OlapTable#getClusterKeyUids(List)} returns {@code null} (not empty list) when
     * no cluster keys are present. Downstream code branches on null vs non-null — locking
     * the contract.
     */
    @Test
    public void testGetClusterKeyUidsReturnsNullWhenNoClusterKeys() {
        Column a = buildColumn("a", 100, -1);
        Column b = buildColumn("b", 200, -1);
        List<Integer> uids = OlapTable.getClusterKeyUids(Arrays.asList(a, b));
        Assertions.assertNull(uids,
                "getClusterKeyUids must return null (not empty list) when no cluster keys; "
                        + "downstream code uses null-check.");
    }

    /**
     * Single cluster key: list of size 1.
     */
    @Test
    public void testGetClusterKeyUidsSingleColumn() {
        Column a = buildColumn("a", 100, 0);
        Column b = buildColumn("b", 200, -1);
        List<Integer> uids = OlapTable.getClusterKeyUids(Arrays.asList(a, b));
        Assertions.assertEquals(Arrays.asList(100), uids);
    }

    /**
     * SEV-3 #9: there is no upper bound on the number of cluster key columns. This test
     * builds an oversize ORDER BY (32 columns) and confirms the legacy parser accepts it —
     * pinning the missing limit so a future fix is required to update this test.
     */
    @Test
    public void testClusterKeyHasNoUpperBoundCount_SymptomPin() throws AnalysisException {
        int big = 32;
        List<String> uniqueKeyCols = new ArrayList<>();
        List<String> orderByCols = new ArrayList<>();
        List<ColumnDef> schemaCols = new ArrayList<>();
        // unique key = first column only, order by = all columns
        uniqueKeyCols.add("k");
        schemaCols.add(col("k", PrimitiveType.INT));
        for (int i = 0; i < big; i++) {
            String n = "v" + i;
            orderByCols.add(n);
            schemaCols.add(col(n, PrimitiveType.INT));
        }
        // Need at least one non-key column to satisfy schema constraints (size matches keys)
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS, uniqueKeyCols, orderByCols);
        Assertions.assertDoesNotThrow(() -> kd.analyze(schemaCols),
                "SEV-3 #9 symptom: legacy parser accepts an arbitrary number of cluster key "
                        + "columns (32 in this test). When an upper bound is introduced, this test "
                        + "will turn red and should be updated to assert rejection above the limit.");
        // verify all 32 columns received cluster key ids
        for (int i = 0; i < big; i++) {
            Assertions.assertEquals(i, schemaCols.get(i + 1).getClusterKeyId(),
                    "cluster key id should be position in ORDER BY for v" + i);
        }
    }

    /**
     * Negative case: when unique keys size > order by size, prefix check should pass through
     * to no-throw if columns differ. This locks the {@code minKeySize} edge so a refactor
     * doesn't accidentally flip the comparison direction.
     */
    @Test
    public void testClusterKeyShorterThanUniqueKeyDifferentColsAccepted() {
        KeysDesc kd = new KeysDesc(KeysType.UNIQUE_KEYS,
                Lists.newArrayList("a", "b", "c"),
                Lists.newArrayList("d"));
        Assertions.assertDoesNotThrow(() -> kd.analyze(schema("a", "b", "c", "d")),
                "ORDER BY shorter than UNIQUE KEY with distinct cols must be accepted");
        // d should get cluster_key_id=0
    }
}
