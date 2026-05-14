// AGG-NAN-003 (Second-pass review · R4: WHERE v != v should detect NaN per IEEE)
// Spec: IEEE 754: NaN != NaN → true。`WHERE v != v` 是 NaN 检测标准 trick，应返 NaN 行数。
// 当前 4.1: 返 0 行（NaN != NaN 评估为 false）→ FAIL
suite("repro_agg_nan_003") {
    sql "DROP TABLE IF EXISTS t_agg_nan_003"
    try {
        sql """
            CREATE TABLE t_agg_nan_003 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_nan_003 VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, 2.0)"
        def r = sql "SELECT COUNT(*) FROM t_agg_nan_003 WHERE v != v"
        assertEquals("1", r[0][0].toString(),
            "WHERE v != v must return NaN rows count per IEEE 754 (standard NaN detect trick)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_nan_003" } catch (Exception ignore) {}
    }
}
