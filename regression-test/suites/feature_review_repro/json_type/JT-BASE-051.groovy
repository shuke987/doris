// JT-BASE-051: SQL NULL vs JSON null 区分
suite("repro_jt_base_051") {
    sql "DROP TABLE IF EXISTS t_jt_base_051"
    try {
        sql """
            CREATE TABLE t_jt_base_051 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_051 VALUES (1, NULL), (2, 'null')"
        def r = sql "SELECT id, j IS NULL FROM t_jt_base_051 ORDER BY id"
        assertEquals(2, r.size(), "JT-BASE-051: 2 rows expected; observed=${r}")
        assertEquals("true", r[0][1].toString().toLowerCase(), "JT-BASE-051: id=1 SQL NULL → IS NULL true; observed=${r}")
        assertEquals("false", r[1][1].toString().toLowerCase(), "JT-BASE-051: id=2 JSON null → IS NULL false; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_051" } catch (Exception ignore) {}
    }
}
