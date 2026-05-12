// JT-EXTRACT-032: vec-scalar
suite("repro_jt_extract_032") {
    sql "DROP TABLE IF EXISTS t_jt_extract_032"
    try {
        sql """
            CREATE TABLE t_jt_extract_032 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_extract_032 VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}')"
        def r = sql "SELECT id, jsonb_extract(j, '\$.a') FROM t_jt_extract_032 ORDER BY id"
        assertEquals(2, r.size(), "JT-EXTRACT-032; observed=${r}")
        assertEquals("1", r[0][1].toString(), "JT-EXTRACT-032 row1; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_extract_032" } catch (Exception ignore) {}
    }
}
