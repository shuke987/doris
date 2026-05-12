// JT-BASE-013: UNIQUE KEY(id) + JSONB value REPLACE 列 应成功
suite("repro_jt_base_013") {
    sql "DROP TABLE IF EXISTS t_jt_base_013"
    try {
        sql """
            CREATE TABLE t_jt_base_013 (id INT, j JSONB)
            UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_013 VALUES (1, '{\"a\":1}')"
        def r = sql "SELECT id FROM t_jt_base_013"
        assertEquals(1, r.size(), "JT-BASE-013: row missing; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_013" } catch (Exception ignore) {}
    }
}
