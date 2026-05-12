// JT-PARSE-093: SELECT + jsonb_parse (FAIL) 含非法行 — SEV-1 #1 核心
suite("repro_jt_parse_093") {
    sql "DROP TABLE IF EXISTS t_jt_parse_093"
    try {
        sql """
            CREATE TABLE t_jt_parse_093 (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_parse_093 VALUES (1,'{\"a\":1}'),(2,'{a:'),(3,'{\"b\":2}')"
        boolean threw = false
        try { sql "SELECT jsonb_parse(s) FROM t_jt_parse_093" }
        catch (Exception e) { threw = true }
        // spec: SELECT with invalid row should FAIL query
        assertTrue(threw,
            "JT-PARSE-093 (SEV-1 #1): SELECT jsonb_parse with malformed row should throw")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_093" } catch (Exception ignore) {}
    }
}
