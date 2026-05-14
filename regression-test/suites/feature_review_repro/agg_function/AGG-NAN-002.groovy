// AGG-NAN-002 (Second-pass review · R3: WHERE v = NaN matches NaN rows — IEEE violation)
// Spec: IEEE 754: NaN ≠ 任何值（含自己）→ `WHERE v = NaN` 必须返 0 行。
// 当前 4.1: 返 1 行（把 NaN 当成相等）→ FAIL (HARD RULE: assert correct)
suite("repro_agg_nan_002") {
    sql "DROP TABLE IF EXISTS t_agg_nan_002"
    try {
        sql """
            CREATE TABLE t_agg_nan_002 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_nan_002 VALUES (1, 1.0), (2, CAST('nan' AS DOUBLE)), (3, 2.0)"
        def r = sql "SELECT COUNT(*) FROM t_agg_nan_002 WHERE v = CAST('nan' AS DOUBLE)"
        assertEquals("0", r[0][0].toString(),
            "WHERE v = NaN must match 0 rows per IEEE 754 (NaN != NaN); explicit isnan() should be used to detect NaN")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_nan_002" } catch (Exception ignore) {}
    }
}
