// AGG-SEV1-002 (SEV-2 to_bitmap negative silent drop)
// Oracle: bitmap 存非负整数；负数应该返 NULL（明确）或保留语义
// 实际：返回 EMPTY bitmap（NOT NULL）→ BITMAP_UNION 静默丢失负数 → 召回错误
suite("repro_agg_sev1_002") {
    // to_bitmap(-1) 返 NOT NULL 但 BITMAP_COUNT=0
    def r1 = sql "SELECT to_bitmap(-1) IS NULL"
    assertEquals(false, r1[0][0],
        "SEV-2: to_bitmap(negative) returns NOT NULL (should be NULL for clear signal); silent drop")
    def r2 = sql "SELECT BITMAP_COUNT(to_bitmap(-1))"
    assertEquals(0L, r2[0][0],
        "SEV-2: to_bitmap(negative) → empty bitmap (silent data loss)")

    // 在 INSERT/UNION pipeline 中：BITMAP_UNION on data with negatives loses them
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
        assertNotEquals(truth[0][0], actual[0][0],
            "SEV-2: BITMAP_UNION_COUNT ≠ COUNT(DISTINCT) due to negative drop; actual=${actual[0][0]} truth=${truth[0][0]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_002" } catch (Exception ignore) {}
    }
}
