// JT-BASE-017: AGGREGATE + JSONB REPLACE_IF_NOT_NULL
suite("repro_jt_base_017") {
    sql "DROP TABLE IF EXISTS t_jt_base_017"
    try {
        sql """
            CREATE TABLE t_jt_base_017 (id INT, j JSONB REPLACE_IF_NOT_NULL)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_017 VALUES (1,'{\"a\":1}')"
        def r = sql "SELECT count(*) FROM t_jt_base_017"
        assertEquals("1", r[0][0].toString(), "JT-BASE-017; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_017" } catch (Exception ignore) {}
    }
}
