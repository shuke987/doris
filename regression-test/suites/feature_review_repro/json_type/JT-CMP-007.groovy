// JT-CMP-007: WHERE j IS NULL on JSONB
suite("repro_jt_cmp_007") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_007"
    try {
        sql """
            CREATE TABLE t_jt_cmp_007 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_007 VALUES (1,NULL),(2,'{\"a\":1}'),(3,NULL)"
        def r = sql "SELECT count(*) FROM t_jt_cmp_007 WHERE j IS NULL"
        assertEquals("2", r[0][0].toString(), "JT-CMP-007; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_007" } catch (Exception ignore) {}
    }
}
