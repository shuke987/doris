// JT-CROSS-041: JSONB extract in ORDER BY
suite("repro_jt_cross_041") {
    sql "DROP TABLE IF EXISTS t_jt_cross_041"
    try {
        sql """
            CREATE TABLE t_jt_cross_041 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_041 VALUES (1,'{\"a\":3}'),(2,'{\"a\":1}'),(3,'{\"a\":2}')"
        def r = sql "SELECT id FROM t_jt_cross_041 ORDER BY jsonb_extract_int(j, '\$.a')"
        assertEquals(3, r.size(), "JT-CROSS-041; observed=${r}")
        assertEquals("2", r[0][0].toString(), "JT-CROSS-041 first; observed=${r}")
        assertEquals("1", r[2][0].toString(), "JT-CROSS-041 last; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_041" } catch (Exception ignore) {}
    }
}
