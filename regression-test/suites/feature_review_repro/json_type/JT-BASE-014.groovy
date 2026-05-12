// JT-BASE-014: AGGREGATE KEY + JSONB value REPLACE 列
suite("repro_jt_base_014") {
    sql "DROP TABLE IF EXISTS t_jt_base_014"
    try {
        sql """
            CREATE TABLE t_jt_base_014 (id INT, j JSONB REPLACE)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_014 VALUES (1,'{\"a\":1}')"
        def r = sql "SELECT count(*) FROM t_jt_base_014"
        assertEquals("1", r[0][0].toString(), "JT-BASE-014; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_014" } catch (Exception ignore) {}
    }
}
