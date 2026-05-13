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

// Feature review UT for cluster_key (UNIQUE_KEYS + MOW + ORDER BY).
//
// Pilot context: SOP feature review on branch-4.1. Three SEVs were confirmed
// from FE-side review (see /shuke/quality-analysis/feature-review/cluster_key/
// review.md). The BE side of the same feature reads cluster_key_uids from
// TabletSchemaPB and drives:
//   1. segment sort order (segment_writer._is_mow_with_cluster_key)
//   2. memtable sort order  (memtable._sort_by_cluster_keys)
//   3. vertical compaction column grouping (Merger::vertical_split_columns)
//   4. iterator merge ordering (vertical_merge_iterator)
//
// These tests lock the *current* BE behavior so we can detect either:
//   (a) regressions if BE-side semantics drift, or
//   (b) FE/BE contract violations once the FE bugs are fixed (BE must continue
//       to tolerate the wider range of valid inputs the FE will start passing).
//
// Test name pattern: ClusterKeyFeatureReviewTest.CK_XXX_<short_name>
//
// Hard-spec contract:
//   PASS = current observed behavior (locked-in)
//   FAIL = behavior changed / SEV regression

#include <gtest/gtest.h>

#include <algorithm>
#include <cstdint>
#include <memory>
#include <string>
#include <vector>

#include "exec/operator/exchange_sink_buffer.h"
#include "exprs/function/simple_function_factory.h"
#include "gen_cpp/olap_file.pb.h"
#include "storage/merger.h"
#include "storage/olap_common.h"
#include "storage/tablet/tablet_schema.h"

// --- Minimal stubs for BE_TEST-only externs referenced by libExec / libExprs.
// Mirrors the pattern used by other mini-target tests (iia, etc.). Since this
// mini executable doesn't link the .cpp files that normally provide these
// definitions, we supply no-ops to satisfy the linker.
namespace doris {
void transmit_blockv2(PBackendService_Stub* /*stub*/,
                      std::unique_ptr<AutoReleaseClosure<PTransmitDataParams,
                                                         ExchangeSendCallback<PTransmitDataResult>>>
                              /*closure*/) {
    // intentional no-op for cluster_key schema-only unit tests
}
void register_function_throw_exception(SimpleFunctionFactory& /*factory*/) {
    // intentional no-op for cluster_key schema-only unit tests
}
} // namespace doris

namespace doris {

// -----------------------------------------------------------------------------
// Helpers to build a TabletSchemaPB exercising cluster_key configurations.
// -----------------------------------------------------------------------------

namespace {

// Add a single column (unique_id, name, INT type, is_key flag) to a PB.
void add_int_column(TabletSchemaPB* pb, int32_t uid, const std::string& name, bool is_key) {
    auto* col = pb->add_column();
    col->set_unique_id(uid);
    col->set_name(name);
    col->set_type("INT");
    col->set_is_key(is_key);
    col->set_is_nullable(false);
    col->set_length(4);
    col->set_index_length(4);
    col->set_aggregation(is_key ? "NONE" : "REPLACE");
    col->set_precision(10);
    col->set_frac(0);
    col->set_is_bf_column(false);
    col->set_visible(true);
}

// Build a base TabletSchemaPB with UNIQUE_KEYS, 2 key columns + 2 value columns.
// Caller sets cluster_key_uids on the returned PB.
TabletSchemaPB make_uniq_pb(int num_keys = 2, int num_values = 2) {
    TabletSchemaPB pb;
    pb.set_keys_type(UNIQUE_KEYS);
    pb.set_num_short_key_columns(num_keys);
    for (int i = 0; i < num_keys; ++i) {
        // unique_id = i, name = "k{i}"
        add_int_column(&pb, /*uid*/ i, /*name*/ "k" + std::to_string(i), /*is_key*/ true);
    }
    for (int i = 0; i < num_values; ++i) {
        add_int_column(&pb, /*uid*/ num_keys + i, /*name*/ "v" + std::to_string(i),
                       /*is_key*/ false);
    }
    return pb;
}

} // namespace

class ClusterKeyFeatureReviewTest : public ::testing::Test {};

// =============================================================================
// CK-001..006: TabletSchema parses cluster_key_uids from TabletSchemaPB.
// Locks the contract: init_from_pb preserves the cluster_key_uids order and
// values exactly as written; no de-dup, no sort, no bounds check.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_001_empty_cluster_key_uids) {
    auto pb = make_uniq_pb();
    // No cluster_key_uids set.
    TabletSchema schema;
    schema.init_from_pb(pb);

    EXPECT_TRUE(schema.cluster_key_uids().empty())
            << "schema with no cluster_key_uids in PB must have empty vector";
    EXPECT_EQ(schema.keys_type(), UNIQUE_KEYS);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_002_single_cluster_key_value_col) {
    // cluster_key on a *value* column (uid=2 -> v0): valid: cluster key can be
    // any column except keys when there's only one (FE may forbid this; BE
    // currently tolerates).
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(2); // v0

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 1U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 2U);
    // field_index by uid must locate v0 at column index 2.
    EXPECT_EQ(schema.field_index(2), 2);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_003_multi_cluster_keys_preserve_order) {
    // cluster_key (v0, k1, v1) -- order matters for segment sort.
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(2); // v0
    pb.add_cluster_key_uids(1); // k1
    pb.add_cluster_key_uids(3); // v1

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 3U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 2U);
    EXPECT_EQ(schema.cluster_key_uids()[1], 1U);
    EXPECT_EQ(schema.cluster_key_uids()[2], 3U);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_004_cluster_keys_with_duplicates_preserved) {
    // BE does NOT de-dup cluster_key_uids. FE should reject duplicates but
    // if it ever ships a buggy schema, BE will faithfully replicate the dup.
    // This locks "no defensive de-dup" behavior.
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(2);
    pb.add_cluster_key_uids(2);

    TabletSchema schema;
    schema.init_from_pb(pb);

    EXPECT_EQ(schema.cluster_key_uids().size(), 2U)
            << "BE must not silently de-dup cluster_key_uids";
    EXPECT_EQ(schema.cluster_key_uids()[0], 2U);
    EXPECT_EQ(schema.cluster_key_uids()[1], 2U);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_005_cluster_keys_out_of_range_uid_init_succeeds) {
    // SEV-2-like defensive check on BE side: schema parses fine even if a
    // cluster_key_uid references a non-existent column. The bad uid only
    // surfaces later at field_index() -> -1, which segment_writer.cpp:779
    // DCHECKs against. Lock current behavior: init_from_pb does NOT validate.
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(999); // no such column

    TabletSchema schema;
    schema.init_from_pb(pb);

    EXPECT_EQ(schema.cluster_key_uids().size(), 1U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 999U);
    EXPECT_EQ(schema.field_index(999), -1)
            << "field_index returns -1 for unknown uid (segment_writer will hit DCHECK)";
}

TEST_F(ClusterKeyFeatureReviewTest, CK_006_many_cluster_keys) {
    // Stress: 8 cluster keys (e.g., a wide ORDER BY).
    auto pb = make_uniq_pb(/*num_keys*/ 1, /*num_values*/ 8);
    for (int uid = 1; uid <= 8; ++uid) {
        pb.add_cluster_key_uids(uid);
    }
    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 8U);
    for (int i = 0; i < 8; ++i) {
        EXPECT_EQ(schema.cluster_key_uids()[i], static_cast<uint32_t>(i + 1));
    }
}

// =============================================================================
// CK-010..014: SEV-2 #6 "dense clusterKeyId" equivalent on BE side.
// FE bug: OlapTable.getClusterKeyUids assumes clusterKeyId is dense (0,1,2..).
// On the BE side the corresponding logic is "for each uid in cluster_key_uids,
// look up via field_index(uid)". This is robust to sparse / non-monotonic uid
// sets, so BE is the safety net if FE breaks. These tests assert BE robustness.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_010_sparse_uids_field_index_resolves_correctly) {
    // Build a schema where column unique_ids are not 0..N-1 (e.g., after some
    // adds/drops). Cluster keys reference uids by their actual values.
    TabletSchemaPB pb;
    pb.set_keys_type(UNIQUE_KEYS);
    pb.set_num_short_key_columns(2);
    add_int_column(&pb, /*uid*/ 10, "k0", true);
    add_int_column(&pb, /*uid*/ 20, "k1", true);
    add_int_column(&pb, /*uid*/ 30, "v0", false);
    add_int_column(&pb, /*uid*/ 40, "v1", false);
    add_int_column(&pb, /*uid*/ 50, "v2", false);

    // cluster_key = (v2 by uid=50, k1 by uid=20, v0 by uid=30)
    pb.add_cluster_key_uids(50);
    pb.add_cluster_key_uids(20);
    pb.add_cluster_key_uids(30);

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 3U);
    // Resolve via field_index by uid -> ordinal position.
    EXPECT_EQ(schema.field_index(50), 4); // v2 at ordinal 4
    EXPECT_EQ(schema.field_index(20), 1); // k1 at ordinal 1
    EXPECT_EQ(schema.field_index(30), 2); // v0 at ordinal 2
}

TEST_F(ClusterKeyFeatureReviewTest, CK_011_sparse_uids_after_field_index_lookup) {
    // Same as above but verify the *iteration pattern* used in
    // memtable.cpp:_sort_by_cluster_keys actually works and produces the
    // expected sort-column-ordinal sequence.
    TabletSchemaPB pb;
    pb.set_keys_type(UNIQUE_KEYS);
    pb.set_num_short_key_columns(1);
    add_int_column(&pb, /*uid*/ 100, "k0", true);
    add_int_column(&pb, /*uid*/ 200, "v_a", false);
    add_int_column(&pb, /*uid*/ 300, "v_b", false);
    add_int_column(&pb, /*uid*/ 400, "v_c", false);

    // cluster_key = (v_b, v_a) -> sort by v_b then v_a
    pb.add_cluster_key_uids(300);
    pb.add_cluster_key_uids(200);

    TabletSchema schema;
    schema.init_from_pb(pb);

    // Simulate memtable._sort_by_cluster_keys's loop.
    std::vector<int> sort_col_ordinals;
    for (auto cid : schema.cluster_key_uids()) {
        int idx = schema.field_index(static_cast<int32_t>(cid));
        ASSERT_GE(idx, 0) << "cluster_key uid=" << cid << " missing in schema";
        sort_col_ordinals.push_back(idx);
    }
    ASSERT_EQ(sort_col_ordinals.size(), 2U);
    EXPECT_EQ(sort_col_ordinals[0], 2); // v_b
    EXPECT_EQ(sort_col_ordinals[1], 1); // v_a
}

TEST_F(ClusterKeyFeatureReviewTest, CK_012_cluster_key_uid_zero_is_valid) {
    // Cluster key on uid=0 (the first column) is valid -- there's no special
    // "0 means unset" sentinel for cluster_key_uids.
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(0); // k0

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 1U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 0U);
    EXPECT_EQ(schema.field_index(0), 0);
}

// =============================================================================
// CK-020..024: Merger::vertical_split_columns -- the column-grouping logic for
// vertical compaction in MOW-with-cluster-key tables.
//
// Code: be/src/storage/merger.cpp:170-220
// Contract:
//   key_columns = [0 .. num_key_cols)
//   if UNIQUE_KEYS:
//     append sequence_col_idx and delete_sign_idx if present
//     append all cluster_key column ordinals that are >= num_key_cols
//   key_group_cluster_key_idxes = for each cluster_key uid, the *position* of
//                                 its ordinal inside key_columns vector.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_020_vertical_split_no_cluster_key) {
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2);
    TabletSchema schema;
    schema.init_from_pb(pb);

    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);

    ASSERT_GE(column_groups.size(), 1U);
    // First group is the key group: just the 2 key columns.
    EXPECT_EQ(column_groups[0], (std::vector<uint32_t> {0, 1}));
    EXPECT_TRUE(key_group_cluster_key_idxes.empty())
            << "no cluster_key configured -> idxes vector stays empty";
}

TEST_F(ClusterKeyFeatureReviewTest, CK_021_vertical_split_cluster_key_subset_of_keys) {
    // cluster_key = (k1, k0) -- all cluster key columns are already key
    // columns. Per code: "if (idx >= num_key_cols) key_columns.emplace_back",
    // so no extra columns are appended. But key_group_cluster_key_idxes still
    // gets the positions inside the key_columns vector.
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2);
    pb.add_cluster_key_uids(1); // k1
    pb.add_cluster_key_uids(0); // k0
    TabletSchema schema;
    schema.init_from_pb(pb);

    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);

    ASSERT_GE(column_groups.size(), 1U);
    // key_columns = [0,1] (unchanged); positions of (k1=1, k0=0) inside that = [1, 0].
    EXPECT_EQ(column_groups[0], (std::vector<uint32_t> {0, 1}));
    ASSERT_EQ(key_group_cluster_key_idxes.size(), 2U);
    EXPECT_EQ(key_group_cluster_key_idxes[0], 1U);
    EXPECT_EQ(key_group_cluster_key_idxes[1], 0U);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_022_vertical_split_cluster_key_includes_value_col) {
    // cluster_key = (v0, k1) -- v0 is a value column, must get appended to
    // key_columns. From merger.cpp comment:
    //   schema uids [1,2,5,3,6,4], keys [1,2]
    //   cluster_keys [3,1,4] -> key_columns become [0,1,3,5] and
    //   key_group_cluster_key_idxes = [2,1,3]
    // Here we use a simpler 4-col schema: uids 0..3, keys (0,1), cluster (2,1)
    // -> key_columns = [0,1,2], key_group_cluster_key_idxes = [2, 1].
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2);
    pb.add_cluster_key_uids(2); // v0
    pb.add_cluster_key_uids(1); // k1
    TabletSchema schema;
    schema.init_from_pb(pb);

    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);

    ASSERT_GE(column_groups.size(), 1U);
    // key_columns should be [0, 1, 2] (v0 appended).
    EXPECT_EQ(column_groups[0], (std::vector<uint32_t> {0, 1, 2}));
    ASSERT_EQ(key_group_cluster_key_idxes.size(), 2U);
    EXPECT_EQ(key_group_cluster_key_idxes[0], 2U); // v0 at position 2
    EXPECT_EQ(key_group_cluster_key_idxes[1], 1U); // k1 at position 1
}

TEST_F(ClusterKeyFeatureReviewTest, CK_023_vertical_split_value_cols_excluded_from_groups) {
    // Verify v_col placement: with cluster key on v0 (uid=2), v0 should NOT
    // appear in any value group (it was promoted to key group). v1 stays as
    // value column.
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2);
    pb.add_cluster_key_uids(2); // v0
    TabletSchema schema;
    schema.init_from_pb(pb);

    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);

    ASSERT_GE(column_groups.size(), 1U);
    // Key group should contain v0 (ordinal 2).
    EXPECT_NE(std::find(column_groups[0].begin(), column_groups[0].end(), 2u),
              column_groups[0].end())
            << "v0 (ordinal 2) must be promoted into the key group";

    // No subsequent group should contain ordinal 2.
    for (size_t gi = 1; gi < column_groups.size(); ++gi) {
        EXPECT_EQ(std::find(column_groups[gi].begin(), column_groups[gi].end(), 2u),
                  column_groups[gi].end())
                << "v0 (ordinal 2) must not appear in value group " << gi;
    }
}

TEST_F(ClusterKeyFeatureReviewTest, CK_024_vertical_split_no_dedup_on_repeated_cluster_uids) {
    // If FE were to ship cluster_key_uids = [2, 2] (duplicate), the inner
    // "if (idx >= num_key_cols)" only adds ordinal 2 once (because
    // emplace_back is gated on a single equality, no membership check).
    // Actually re-reading the code: the loop does:
    //   for cid in cluster_key_uids:
    //     idx = field_index(cid)
    //     if (idx >= num_key_cols) key_columns.emplace_back(idx)
    // -> duplicates WILL be appended. Then the second loop pushes
    // key_group_cluster_key_idxes positions; for the 2nd dup, the position
    // found is the FIRST occurrence (break-on-match).
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2);
    pb.add_cluster_key_uids(2);
    pb.add_cluster_key_uids(2);
    TabletSchema schema;
    schema.init_from_pb(pb);

    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);

    ASSERT_GE(column_groups.size(), 1U);
    // Lock current (buggy?) behavior: ordinal 2 appended twice to key_columns.
    int count_2 = 0;
    for (auto v : column_groups[0]) {
        if (v == 2) count_2++;
    }
    EXPECT_EQ(count_2, 2) << "current BE behavior: duplicate cluster_key uid produces "
                            "duplicate ordinal in key_columns (no de-dup defense)";
    // Both idxes resolve to position 2 in key_columns (the FIRST occurrence
    // due to "break" on match).
    ASSERT_EQ(key_group_cluster_key_idxes.size(), 2U);
    EXPECT_EQ(key_group_cluster_key_idxes[0], 2U);
    EXPECT_EQ(key_group_cluster_key_idxes[1], 2U);
}

// =============================================================================
// CK-030..034: copy_from / serialize round-trip of cluster_key_uids.
// Ensures schema cloning (used by schema change, partial updates, etc.)
// preserves the cluster_key_uids field correctly.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_030_roundtrip_to_schema_pb) {
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(2);
    pb.add_cluster_key_uids(1);
    pb.add_cluster_key_uids(3);

    TabletSchema schema;
    schema.init_from_pb(pb);

    TabletSchemaPB out_pb;
    schema.to_schema_pb(&out_pb);
    ASSERT_EQ(out_pb.cluster_key_uids_size(), 3);
    EXPECT_EQ(out_pb.cluster_key_uids(0), 2);
    EXPECT_EQ(out_pb.cluster_key_uids(1), 1);
    EXPECT_EQ(out_pb.cluster_key_uids(2), 3);

    // Re-load and verify.
    TabletSchema schema2;
    schema2.init_from_pb(out_pb);
    EXPECT_EQ(schema2.cluster_key_uids(), schema.cluster_key_uids());
}

TEST_F(ClusterKeyFeatureReviewTest, CK_031_roundtrip_empty_cluster_key_uids) {
    auto pb = make_uniq_pb();
    TabletSchema schema;
    schema.init_from_pb(pb);

    TabletSchemaPB out_pb;
    schema.to_schema_pb(&out_pb);
    EXPECT_EQ(out_pb.cluster_key_uids_size(), 0);
}

TEST_F(ClusterKeyFeatureReviewTest, CK_032_copy_from_preserves_cluster_keys) {
    auto pb = make_uniq_pb();
    pb.add_cluster_key_uids(3);
    pb.add_cluster_key_uids(2);

    TabletSchema src;
    src.init_from_pb(pb);

    TabletSchema dst;
    dst.copy_from(src);

    EXPECT_EQ(dst.cluster_key_uids(), src.cluster_key_uids());
    ASSERT_EQ(dst.cluster_key_uids().size(), 2U);
    EXPECT_EQ(dst.cluster_key_uids()[0], 3U);
    EXPECT_EQ(dst.cluster_key_uids()[1], 2U);
}

// =============================================================================
// CK-040..041: Sanity for the "MOW + cluster_key" interaction at schema level.
// The MOW flag itself lives on TabletMeta (out of scope for this mini target),
// but we can verify the schema-level building blocks line up.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_040_cluster_key_requires_unique_keys_at_pb_level) {
    // BE does NOT enforce keys_type=UNIQUE_KEYS at TabletSchema parse time --
    // the enforcement is on the FE. Lock that BE will faithfully store
    // cluster_key_uids even on a DUP_KEYS schema (invariant violation by FE).
    TabletSchemaPB pb;
    pb.set_keys_type(DUP_KEYS); // intentionally wrong
    pb.set_num_short_key_columns(1);
    add_int_column(&pb, 0, "k0", true);
    add_int_column(&pb, 1, "v0", false);
    pb.add_cluster_key_uids(1);

    TabletSchema schema;
    schema.init_from_pb(pb);

    EXPECT_EQ(schema.keys_type(), DUP_KEYS);
    ASSERT_EQ(schema.cluster_key_uids().size(), 1U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 1U);
    // Note: downstream segment_writer.cpp:_is_mow_with_cluster_key requires
    // BOTH _is_mow() (which checks UNIQUE_KEYS) AND non-empty cluster keys,
    // so even this malformed schema won't actually drive cluster-key sort.
    // This test just locks the parser-level laxness.
}

TEST_F(ClusterKeyFeatureReviewTest, CK_041_cluster_key_overlap_with_short_key_prefix) {
    // SEV-1 #1 BE projection: FE wrongly rejects UNIQUE KEY(a,b) + ORDER BY(a,b,c)
    // (KeysDesc.sameKey). If FE is fixed, BE will start receiving schemas where
    // cluster_key uids are a SUPERSET of (or equal to) the unique key uids.
    // BE must handle this without complaint.
    auto pb = make_uniq_pb(/*num_keys*/ 2, /*num_values*/ 2); // keys k0,k1 ; values v0,v1
    pb.add_cluster_key_uids(0); // k0 (== unique key)
    pb.add_cluster_key_uids(1); // k1 (== unique key)
    pb.add_cluster_key_uids(2); // v0 (extension)

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 3U);
    EXPECT_EQ(schema.cluster_key_uids()[0], 0U);
    EXPECT_EQ(schema.cluster_key_uids()[1], 1U);
    EXPECT_EQ(schema.cluster_key_uids()[2], 2U);

    // Now run the actual merger split; v0 must promote into the key group
    // while k0/k1 stay there.
    std::vector<std::vector<uint32_t>> column_groups;
    std::vector<uint32_t> key_group_cluster_key_idxes;
    Merger::vertical_split_columns(schema, &column_groups, &key_group_cluster_key_idxes,
                                   /*num_columns_per_group*/ 4);
    ASSERT_GE(column_groups.size(), 1U);
    EXPECT_EQ(column_groups[0], (std::vector<uint32_t> {0, 1, 2}));
    ASSERT_EQ(key_group_cluster_key_idxes.size(), 3U);
    EXPECT_EQ(key_group_cluster_key_idxes[0], 0U); // k0 at position 0
    EXPECT_EQ(key_group_cluster_key_idxes[1], 1U); // k1 at position 1
    EXPECT_EQ(key_group_cluster_key_idxes[2], 2U); // v0 at position 2
}

// =============================================================================
// CK-050: cluster_key_uids holes (sparse / non-contiguous in PB) — defensive
// pattern for SEV-2 #6 on the BE side.
// =============================================================================

TEST_F(ClusterKeyFeatureReviewTest, CK_050_cluster_key_uids_non_monotonic) {
    // FE SEV-2 #6: getClusterKeyUids assumes clusterKeyId is dense (0..N-1).
    // On the BE side cluster_key_uids is a free list of column unique_ids
    // (not the FE-internal cluster_key_id). So sparse / non-monotonic values
    // here are fine; we just store them. This test locks that contract.
    auto pb = make_uniq_pb(/*num_keys*/ 1, /*num_values*/ 5);
    // 5 value cols uid 1..5. cluster_key picks them in non-monotonic order.
    pb.add_cluster_key_uids(5);
    pb.add_cluster_key_uids(2);
    pb.add_cluster_key_uids(4);
    pb.add_cluster_key_uids(1);
    pb.add_cluster_key_uids(3);

    TabletSchema schema;
    schema.init_from_pb(pb);

    ASSERT_EQ(schema.cluster_key_uids().size(), 5U);
    // Order preserved exactly.
    EXPECT_EQ(schema.cluster_key_uids()[0], 5U);
    EXPECT_EQ(schema.cluster_key_uids()[1], 2U);
    EXPECT_EQ(schema.cluster_key_uids()[2], 4U);
    EXPECT_EQ(schema.cluster_key_uids()[3], 1U);
    EXPECT_EQ(schema.cluster_key_uids()[4], 3U);

    // Every uid resolves to a valid ordinal.
    for (auto uid : schema.cluster_key_uids()) {
        EXPECT_GE(schema.field_index(static_cast<int32_t>(uid)), 0);
    }
}

} // namespace doris
