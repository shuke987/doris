// JT-BASE-003: NULLABLE JSONB + INSERT NULL
suite("repro_jt_base_003") {
    sql "DROP TABLE IF EXISTS t_jt_base_003"
    try {
        sql """
            CREATE TABLE t_jt_base_003 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_003 VALUES (1, NULL)"
        def r = sql "SELECT j IS NULL FROM t_jt_base_003 WHERE id=1"
        assertEquals(1, r.size(), "JT-BASE-003: row missing; observed=${r}")
        // Doris regression framework returns true/false string for IS NULL
        assertEquals("true", r[0][0].toString().toLowerCase(),
            "JT-BASE-003: SQL NULL should be NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_003" } catch (Exception ignore) {}
    }
}
