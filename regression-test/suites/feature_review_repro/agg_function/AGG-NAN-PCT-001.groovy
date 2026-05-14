// AGG-NAN-PCT-001 (Second-pass review · PERCENTILE vs PERCENTILE_APPROX NaN inconsistency)
// Spec: 两个 percentile 实现对 NaN 行为应一致（或文档说明差异）。
// 当前 4.1: PERCENTILE(v, 0.5) = nan（传播），PERCENTILE_APPROX(v, 0.5) = 1.5（忽略 NaN）→ 不一致
suite("repro_agg_nan_pct_001") {
    sql "DROP TABLE IF EXISTS t_agg_nan_pct"
    try {
        sql """
            CREATE TABLE t_agg_nan_pct (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_nan_pct VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, 2.0)"
        def re = sql "SELECT PERCENTILE(v, 0.5) FROM t_agg_nan_pct"
        def ra = sql "SELECT PERCENTILE_APPROX(v, 0.5) FROM t_agg_nan_pct"
        def eS = re[0][0].toString().toLowerCase()
        def aS = ra[0][0].toString().toLowerCase()
        boolean bothNaN = eS.contains("nan") && aS.contains("nan")
        boolean bothFinite = !eS.contains("nan") && !aS.contains("nan")
        assertTrue(bothNaN || bothFinite,
            "PERCENTILE / PERCENTILE_APPROX must handle NaN consistently; got exact=${eS} approx=${aS}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_nan_pct" } catch (Exception ignore) {}
    }
}
