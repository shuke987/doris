// AGG-VARS-001 (R5-1: VAR_SAMP / STDDEV_SAMP 在 n=1 时返 0.0, 应返 NULL)
// Spec: Bessel 校正 VAR_SAMP = sum((x-mean)^2) / (n-1); n=1 时 0/0 undefined → NULL.
// Postgres / Snowflake: 返 NULL.
// 当前 4.1: 返 0.0 (与 VAR_POP 混淆) → 误导用户 "样本方差为 0" 实为 "无法计算"
suite("repro_agg_vars_001") {
    sql "DROP TABLE IF EXISTS t_agg_vars_001"
    try {
        sql """
            CREATE TABLE t_agg_vars_001 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_vars_001 VALUES (1, 5.0)"
        def r = sql "SELECT VAR_SAMP(v), STDDEV_SAMP(v), STDDEV(v) FROM t_agg_vars_001"
        // 期望: 三者皆 NULL (n=1 时 Bessel 校正 0/0 undefined)
        assertNull(r[0][0],
            "VAR_SAMP(v) on single row must be NULL (n-1 = 0, undefined); got ${r[0][0]}")
        assertNull(r[0][1],
            "STDDEV_SAMP(v) on single row must be NULL; got ${r[0][1]}")
        assertNull(r[0][2],
            "STDDEV(v) on single row must be NULL (alias of STDDEV_SAMP); got ${r[0][2]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_vars_001" } catch (Exception ignore) {}
    }
}
