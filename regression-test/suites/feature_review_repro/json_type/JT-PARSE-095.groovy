// JT-PARSE-095: INSERT + parse_error_to_null 含非法行 — 错行存 NULL
suite("repro_jt_parse_095") {
    sql "DROP TABLE IF EXISTS t_jt_parse_095_src"
    sql "DROP TABLE IF EXISTS t_jt_parse_095_tgt"
    try {
        sql """
            CREATE TABLE t_jt_parse_095_src (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_parse_095_tgt (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_parse_095_src VALUES (1,'{\"a\":1}'),(2,'{a:'),(3,'{\"b\":2}')"
        sql "INSERT INTO t_jt_parse_095_tgt SELECT id, jsonb_parse_error_to_null(s) FROM t_jt_parse_095_src"
        def r = sql "SELECT count(*) FROM t_jt_parse_095_tgt"
        assertEquals("3", r[0][0].toString(), "JT-PARSE-095: 3 rows; observed=${r}")
        def r2 = sql "SELECT count(*) FROM t_jt_parse_095_tgt WHERE j IS NULL"
        assertEquals("1", r2[0][0].toString(),
            "JT-PARSE-095: 1 NULL (the malformed row); observed=${r2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_095_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_parse_095_tgt" } catch (Exception ignore) {}
    }
}
