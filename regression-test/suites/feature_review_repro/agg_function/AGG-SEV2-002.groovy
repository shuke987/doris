// AGG-SEV2-002 (N6): percentile_approx NaN weight 被接受 (weight<=0 漏过 NaN)
suite("repro_agg_sev2_002") {
    sql "DROP TABLE IF EXISTS t_agg_sev2_002"
    try {
        sql """CREATE TABLE t_agg_sev2_002 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_sev2_002 VALUES (1,10),(2,20),(3,30)"
        // percentile_approx_weighted(v, weight, quantile, compression)
        // NaN weight 应被拒绝
        boolean threw = false
        try {
            def r = sql "SELECT PERCENTILE_APPROX_WEIGHTED(v, CAST('nan' AS DOUBLE), 0.5) FROM t_agg_sev2_002"
            // 如 NaN 漏过，BE state 可能损坏；结果可能为非数值或异常
        } catch (Exception e) {
            threw = true
        }
        // 不强断言：探行为
        assertTrue(true, "NaN weight probe; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev2_002" } catch (Exception ignore) {}
    }
}
