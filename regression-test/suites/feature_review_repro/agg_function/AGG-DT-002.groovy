// AGG-DT-002: BITMAP_UNION_COUNT vs COUNT(DISTINCT) 不变量
// Oracle: 对非负整数集，BITMAP_COUNT(BITMAP_UNION(to_bitmap(v))) = COUNT(DISTINCT v)
suite("repro_agg_dt_002") {
    sql "DROP TABLE IF EXISTS t_agg_dt_002"
    try {
        sql """
            CREATE TABLE t_agg_dt_002 (id INT, v BIGINT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // 全非负整数（bitmap 设计范围内）
        sql "INSERT INTO t_agg_dt_002 VALUES (1,100),(2,200),(3,100),(4,300),(5,200)"
        def r = sql """SELECT
            BITMAP_COUNT(BITMAP_UNION(to_bitmap(v))),
            COUNT(DISTINCT v)
            FROM t_agg_dt_002"""
        assertEquals(r[0][1], r[0][0],
            "INVARIANT: BITMAP_UNION_COUNT = COUNT(DISTINCT) on non-negative integers; bitmap=${r[0][0]} distinct=${r[0][1]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_002" } catch (Exception ignore) {}
    }
}
