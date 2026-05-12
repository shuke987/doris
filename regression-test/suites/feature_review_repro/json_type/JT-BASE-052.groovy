// JT-BASE-052: IS NULL 对 SQL NULL 行命中
suite("repro_jt_base_052") {
    sql "DROP TABLE IF EXISTS t_jt_base_052"
    try {
        sql """
            CREATE TABLE t_jt_base_052 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_052 VALUES (1, NULL), (2, '{\"a\":1}')"
        def r = sql "SELECT id FROM t_jt_base_052 WHERE j IS NULL ORDER BY id"
        assertEquals(1, r.size(), "JT-BASE-052: 1 row hit; observed=${r}")
        assertEquals("1", r[0][0].toString(), "JT-BASE-052: id=1 hit; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_052" } catch (Exception ignore) {}
    }
}
