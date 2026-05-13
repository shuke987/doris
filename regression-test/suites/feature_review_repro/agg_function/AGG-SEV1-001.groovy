// AGG-SEV1-001 (SEV-1 SUM BIGINT overflow silent wrap)
// Oracle: 数学事实 SUM(BIGINT_MAX, BIGINT_MAX) = 2*BIGINT_MAX = 18446744073709551614 > BIGINT range
// 期望（spec 合理）: auto-promote 到 LARGEINT → 正数
// 实际（4.1 current）: 静默 wrap → -2
suite("repro_agg_sev1_001") {
    sql "DROP TABLE IF EXISTS t_agg_sev1_001"
    try {
        sql """
            CREATE TABLE t_agg_sev1_001 (id INT, v BIGINT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_sev1_001 VALUES (1, 9223372036854775807), (2, 9223372036854775807)"
        def r = sql "SELECT SUM(v) FROM t_agg_sev1_001"
        // 锁当前 buggy 行为 = -2 (wrap)
        // 修复后应 = 18446744073709551614 (LARGEINT)
        assertEquals(-2L, r[0][0],
            "SEV-1: SUM(BIGINT) silent wrap on overflow (BUG); should promote to LARGEINT = 18446744073709551614")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_001" } catch (Exception ignore) {}
    }
}
