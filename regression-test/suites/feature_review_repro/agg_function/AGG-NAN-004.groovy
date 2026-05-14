// AGG-NAN-004 (Second-pass review · R5: v IN (NaN) matches NaN — same root cause as v = NaN)
// Spec: 与 `=` 一致，`v IN (NaN)` 必须返 false（NaN 不与任何值（含自己）相等）。
// 当前 4.1: 对 NaN 行返 TRUE → FAIL
suite("repro_agg_nan_004") {
    sql "DROP TABLE IF EXISTS t_agg_nan_004"
    try {
        sql """
            CREATE TABLE t_agg_nan_004 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_nan_004 VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, 2.0)"
        def r = sql "SELECT COUNT(*) FROM t_agg_nan_004 WHERE v IN (CAST('nan' AS DOUBLE))"
        assertEquals("0", r[0][0].toString(),
            "WHERE v IN (NaN) must match 0 rows per IEEE 754; consistent with v = NaN behavior")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_nan_004" } catch (Exception ignore) {}
    }
}
