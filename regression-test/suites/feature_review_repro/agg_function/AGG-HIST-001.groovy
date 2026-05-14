// AGG-HIST-001 (R4-4: LINEAR_HISTOGRAM bucket 边界 FP 精度泄漏到 JSON 输出)
// Spec: LINEAR_HISTOGRAM bucket 边界应是干净的 lower+k*interval 值。
// 用 interval=0.1 + 数据 [0.1, 0.2, 0.3, 0.4] 时，bucket 应是 0.1/0.2/0.3/0.4/0.5
// 当前 4.1: 用 IEEE FP 累加，输出 "0.30000000000000004"（0.1+0.1+0.1 的 FP rounding）
suite("repro_agg_hist_001") {
    sql "DROP TABLE IF EXISTS t_agg_hist_001"
    try {
        sql """
            CREATE TABLE t_agg_hist_001 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_hist_001 VALUES (1, 0.1), (2, 0.2), (3, 0.3), (4, 0.4)"
        def r = sql "SELECT LINEAR_HISTOGRAM(v, 0.1) FROM t_agg_hist_001"
        def json = r[0][0].toString()
        // 期望：bucket 边界值用 decimal 算或四舍五入，不应露出 FP 噪声
        assertFalse(json.contains("0.30000000000000004") || json.contains("0.40000000000000"),
            "LINEAR_HISTOGRAM bucket boundary leaks FP noise; got JSON=${json}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_hist_001" } catch (Exception ignore) {}
    }
}
