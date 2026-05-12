// JT-EXTRACT-033: scalar-vec
suite("repro_jt_extract_033") {
    sql "DROP TABLE IF EXISTS t_jt_extract_033"
    try {
        sql """
            CREATE TABLE t_jt_extract_033 (id INT, p VARCHAR(100))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_extract_033 VALUES (1,'\$.a'),(2,'\$.b')"
        def r = sql "SELECT id, jsonb_extract(CAST('{\"a\":1,\"b\":2}' AS JSONB), p) FROM t_jt_extract_033 ORDER BY id"
        assertEquals(2, r.size(), "JT-EXTRACT-033; observed=${r}")
        assertEquals("1", r[0][1].toString(), "JT-EXTRACT-033 row1; observed=${r}")
        assertEquals("2", r[1][1].toString(), "JT-EXTRACT-033 row2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_extract_033" } catch (Exception ignore) {}
    }
}
