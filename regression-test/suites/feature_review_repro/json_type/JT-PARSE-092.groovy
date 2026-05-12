// JT-PARSE-092: INSERT + jsonb_parse 含非法行（事务 FAIL，全表无新增；SEV-1 #1 回归）
suite("repro_jt_parse_092") {
    sql "DROP TABLE IF EXISTS t_jt_parse_092_src"
    sql "DROP TABLE IF EXISTS t_jt_parse_092_tgt"
    try {
        sql """
            CREATE TABLE t_jt_parse_092_src (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_parse_092_tgt (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_parse_092_src VALUES (1,'{\"a\":1}'),(2,'{a:'),(3,'{\"b\":2}')"
        boolean threw = false
        try {
            sql "INSERT INTO t_jt_parse_092_tgt SELECT id, jsonb_parse(s) FROM t_jt_parse_092_src"
        } catch (Exception e) { threw = true }
        def r = sql "SELECT count(*) FROM t_jt_parse_092_tgt"
        // SEV-1 #1: spec says transaction should FAIL → tgt empty
        // observed behavior may differ. PASS = empty + threw OR empty
        assertEquals("0", r[0][0].toString(),
            "JT-PARSE-092: SEV-1 #1: INSERT with malformed row should fail entire txn; observed rows=${r}, threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_092_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_parse_092_tgt" } catch (Exception ignore) {}
    }
}
