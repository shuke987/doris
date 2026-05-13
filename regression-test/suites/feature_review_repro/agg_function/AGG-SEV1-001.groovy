// AGG-SEV1-001 (SEV-1 SUM BIGINT overflow)
// Spec: SUM 不应静默 wrap 溢出。SUM(BIGINT_MAX × 2) 应 promote 到 LARGEINT 返正确值。
// 当前 4.1: 不 promote → wrap 到 -2 → 本 case 在 fix 前会 FAIL（这是 bug signal）
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
        // 期望（spec correct）：2 * BIGINT_MAX = 18446744073709551614 (LARGEINT)
        assertEquals("18446744073709551614", r[0][0].toString(),
            "SUM(BIGINT) must promote to LARGEINT to avoid overflow; expected 2*BIGINT_MAX = 18446744073709551614")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_001" } catch (Exception ignore) {}
    }
}
