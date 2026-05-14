// AGG-CORR-001 (R5-2: CORR with zero variance 返 0.0, 应返 NULL)
// Spec: CORR(x, y) = cov(x, y) / (stddev(x) * stddev(y)). 若 stddev(x) == 0 (x 恒定), 分母 0/0 undefined → NULL.
// 当前 4.1: 返 0.0 → 误导, 用户以为"x 与 y 无相关", 实际是"无法计算(x 恒定)".
// 危害: 报表/ML 特征工程被静默污染, 用户察觉时已得出错误结论.
suite("repro_agg_corr_001") {
    sql "DROP TABLE IF EXISTS t_agg_corr_001"
    try {
        sql """
            CREATE TABLE t_agg_corr_001 (id INT, x DOUBLE, y DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // x 恒定 100.0 (var(x) = 0), y 单调变化
        sql "INSERT INTO t_agg_corr_001 VALUES (1, 100.0, 1.0), (2, 100.0, 2.0), (3, 100.0, 3.0)"
        def r = sql "SELECT CORR(x, y) FROM t_agg_corr_001"
        // 期望: NULL (分母 stddev(x) = 0)
        assertNull(r[0][0],
            "CORR with zero-variance column must be NULL (undefined); got ${r[0][0]} — silent 0.0 misleads users")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_corr_001" } catch (Exception ignore) {}
    }
}
