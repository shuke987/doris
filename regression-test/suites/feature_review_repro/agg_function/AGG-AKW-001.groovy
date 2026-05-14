// AGG-AKW-001 (R4-1: AGGREGATE KEY storage-level SUM(BIGINT) 静默 wrap, 持久化到磁盘)
// Spec: AGGREGATE KEY 表的 BIGINT SUM 列在 storage 层 compaction 时不应静默 wrap。
// 应该 promote 到 LARGEINT，或拒绝写入溢出值。
// 当前 4.1: storage 层直接 wrap → 磁盘存 -2，任何查询时间都看不到正确值 → 持久化 bug
suite("repro_agg_akw_001") {
    sql "DROP TABLE IF EXISTS t_agg_akw_001"
    try {
        sql """
            CREATE TABLE t_agg_akw_001 (k INT, v BIGINT SUM)
            AGGREGATE KEY(k) DISTRIBUTED BY HASH(k) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_akw_001 VALUES (1, 9223372036854775807), (1, 9223372036854775807)"
        def r = sql "SELECT v FROM t_agg_akw_001 WHERE k=1"
        // 期望（spec correct）: 2 * BIGINT_MAX = 18446744073709551614 (LARGEINT)
        // 或: 拒绝写入溢出值（return error at INSERT）
        // 当前: 磁盘存 -2（wrap）
        assertEquals("18446744073709551614", r[0][0].toString(),
            "AGGREGATE KEY BIGINT SUM must promote at storage layer; current persists wrapped -2 to disk")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_akw_001" } catch (Exception ignore) {}
    }
}
