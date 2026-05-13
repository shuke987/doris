// AGG-SEV1-002 (SEV-2 to_bitmap negative)
// Spec: to_bitmap(负数) 应返 NULL（清晰信号），不应返 NOT NULL 但 empty bitmap
// 当前 4.1: 返 NOT NULL + empty → 本 case 在 fix 前会 FAIL
suite("repro_agg_sev1_002") {
    // 期望: to_bitmap(-1) IS NULL
    def r1 = sql "SELECT to_bitmap(-1) IS NULL"
    assertEquals(true, r1[0][0],
        "to_bitmap(negative) must return NULL for clear error signal; current returns NOT NULL but empty bitmap → silent data loss in BITMAP_UNION pipelines")

    // 期望: BITMAP_UNION 路径下 COUNT(DISTINCT) 一致
    sql "DROP TABLE IF EXISTS t_agg_sev1_002"
    try {
        sql """
            CREATE TABLE t_agg_sev1_002 (id INT, v BIGINT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_sev1_002 VALUES (1,100),(2,-100),(3,0)"
        def actual = sql "SELECT BITMAP_COUNT(BITMAP_UNION(to_bitmap(v))) FROM t_agg_sev1_002"
        def truth = sql "SELECT COUNT(DISTINCT v) FROM t_agg_sev1_002"
        // 期望相等（修复后 — 要么负值算入 BITMAP，要么 spec 明确拒绝建表 / 报错）
        assertEquals(truth[0][0], actual[0][0],
            "BITMAP_UNION_COUNT must equal COUNT(DISTINCT); negative values must not be silently dropped")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_002" } catch (Exception ignore) {}
    }
}
