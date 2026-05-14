// AGG-NAN-001 (Second-pass review · R1: MIN/MAX NaN handling asymmetric)
// Spec: MIN/MAX 对 NaN 行为应一致 — 要么都忽略，要么都传播。
// 当前 4.1: MIN 忽略 NaN（返非 NaN 最小值），MAX 传播 NaN（返 NaN）→ FAIL (HARD RULE)
suite("repro_agg_nan_001") {
    sql "DROP TABLE IF EXISTS t_agg_nan_001"
    try {
        sql """
            CREATE TABLE t_agg_nan_001 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_nan_001 VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, 2.0)"
        def rmin = sql "SELECT MIN(v) FROM t_agg_nan_001"
        def rmax = sql "SELECT MAX(v) FROM t_agg_nan_001"
        // 期望：行为对称（都返 NaN 或都忽略 NaN）。当前 MIN=1.0, MAX=nan → 不对称
        def minS = rmin[0][0].toString().toLowerCase()
        def maxS = rmax[0][0].toString().toLowerCase()
        boolean bothNaN = minS.contains("nan") && maxS.contains("nan")
        boolean bothIgnore = !minS.contains("nan") && !maxS.contains("nan")
        assertTrue(bothNaN || bothIgnore,
            "MIN/MAX NaN handling must be symmetric; got MIN=${minS} MAX=${maxS}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_nan_001" } catch (Exception ignore) {}
    }
}
