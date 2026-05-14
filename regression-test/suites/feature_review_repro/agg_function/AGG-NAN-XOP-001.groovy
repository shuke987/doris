// AGG-NAN-XOP-001 (Second-pass review · cross-operator NaN consistency)
// Spec: 同一表达式 `v = v` 在 WHERE / JOIN / GROUP BY 必须一致。
// 当前 4.1：
//   WHERE  v = NaN     → 匹配 NaN 行（违 IEEE）
//   JOIN   ON a.v=b.v  → NaN 不连接（符合 IEEE）
//   GROUP BY v         → NaN 聚成一组（SQL 惯例）
// → 三种语义共存是 bug。锁定 WHERE 与 JOIN 一致的期望（IEEE 标准）。
suite("repro_agg_nan_xop_001") {
    sql "DROP TABLE IF EXISTS t_agg_xop"
    try {
        sql """
            CREATE TABLE t_agg_xop (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_xop VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, CAST('nan' AS DOUBLE))"
        def w = sql "SELECT COUNT(*) FROM t_agg_xop WHERE v = CAST('nan' AS DOUBLE)"
        def j = sql "SELECT COUNT(*) FROM t_agg_xop a JOIN t_agg_xop b ON a.v = b.v WHERE a.id < b.id"
        // 期望：WHERE 0 行（IEEE），JOIN 0 跨 NaN 配对（IEEE），二者行为一致
        assertEquals("0", w[0][0].toString(),
            "WHERE v=NaN must return 0 rows per IEEE (consistent with JOIN ON a.v=b.v which already does)")
        assertEquals("0", j[0][0].toString(),
            "JOIN ON v=v already follows IEEE — keep as baseline reference")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_xop" } catch (Exception ignore) {}
    }
}
