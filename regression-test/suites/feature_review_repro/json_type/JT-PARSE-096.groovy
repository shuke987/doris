// JT-PARSE-096: INSERT + parse_error_to_value 含非法行 — 错行存 default
suite("repro_jt_parse_096") {
    sql "DROP TABLE IF EXISTS t_jt_parse_096_src"
    sql "DROP TABLE IF EXISTS t_jt_parse_096_tgt"
    try {
        sql """
            CREATE TABLE t_jt_parse_096_src (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_parse_096_tgt (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_parse_096_src VALUES (1,'{\"a\":1}'),(2,'{a:'),(3,'{\"b\":2}')"
        sql "INSERT INTO t_jt_parse_096_tgt SELECT id, jsonb_parse_error_to_value(s, '[\"DEF\"]') FROM t_jt_parse_096_src"
        def r = sql "SELECT count(*) FROM t_jt_parse_096_tgt"
        assertEquals("3", r[0][0].toString(), "JT-PARSE-096; observed=${r}")
        def r2 = sql "SELECT jsonb_extract(j, '\$') FROM t_jt_parse_096_tgt WHERE id=2"
        String v = r2[0][0].toString()
        assertTrue(v.contains("DEF"),
            "JT-PARSE-096: malformed row uses default; observed=${r2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_096_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_parse_096_tgt" } catch (Exception ignore) {}
    }
}
