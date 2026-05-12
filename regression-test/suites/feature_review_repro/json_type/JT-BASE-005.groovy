// JT-BASE-005: JSONB DEFAULT NULL
suite("repro_jt_base_005") {
    sql "DROP TABLE IF EXISTS t_jt_base_005"
    try {
        sql """
            CREATE TABLE t_jt_base_005 (id INT, j JSONB NULL DEFAULT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_005(id) VALUES (1)"
        def r = sql "SELECT j IS NULL FROM t_jt_base_005 WHERE id=1"
        assertEquals(1, r.size(), "JT-BASE-005: row missing")
        assertEquals("true", r[0][0].toString().toLowerCase(),
            "JT-BASE-005: DEFAULT NULL should populate NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_005" } catch (Exception ignore) {}
    }
}
