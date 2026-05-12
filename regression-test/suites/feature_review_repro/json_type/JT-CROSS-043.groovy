// JT-CROSS-043: JSONB chain extract → IS NOT NULL
suite("repro_jt_cross_043") {
    sql "DROP TABLE IF EXISTS t_jt_cross_043"
    try {
        sql """
            CREATE TABLE t_jt_cross_043 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_043 VALUES (1,'{\"a\":1}'),(2,'{\"b\":2}')"
        def r = sql "SELECT count(*) FROM t_jt_cross_043 WHERE jsonb_extract(j, '\$.a') IS NOT NULL"
        assertEquals("1", r[0][0].toString(), "JT-CROSS-043; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_043" } catch (Exception ignore) {}
    }
}
