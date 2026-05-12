// JT-EXTRACT-031: vec-vec extract
suite("repro_jt_extract_031") {
    sql "DROP TABLE IF EXISTS t_jt_extract_031"
    try {
        sql """
            CREATE TABLE t_jt_extract_031 (id INT, j JSONB, p VARCHAR(100))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_extract_031 VALUES (1,'{\"a\":1}','\$.a'),(2,'{\"b\":2}','\$.b')"
        def r = sql "SELECT id, jsonb_extract(j, p) FROM t_jt_extract_031 ORDER BY id"
        assertEquals(2, r.size(), "JT-EXTRACT-031: 2 rows; observed=${r}")
        assertEquals("1", r[0][1].toString(), "JT-EXTRACT-031 row1; observed=${r}")
        assertEquals("2", r[1][1].toString(), "JT-EXTRACT-031 row2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_extract_031" } catch (Exception ignore) {}
    }
}
