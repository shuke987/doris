// JT-CROSS-040: JSONB extract in WHERE EXPR (predicate pushdown)
suite("repro_jt_cross_040") {
    sql "DROP TABLE IF EXISTS t_jt_cross_040"
    try {
        sql """
            CREATE TABLE t_jt_cross_040 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_040 VALUES (1,'{\"key\":\"foo\"}'),(2,'{\"key\":\"bar\"}')"
        def r = sql "SELECT id FROM t_jt_cross_040 WHERE jsonb_extract_string(j, '\$.key') = 'foo'"
        assertEquals(1, r.size(), "JT-CROSS-040; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_040" } catch (Exception ignore) {}
    }
}
