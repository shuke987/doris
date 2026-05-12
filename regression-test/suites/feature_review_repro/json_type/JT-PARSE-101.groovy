// JT-PARSE-101: INSERT 单行非法 — non-strict (default) skip 该行
suite("repro_jt_parse_101") {
    sql "DROP TABLE IF EXISTS t_jt_parse_101"
    try {
        sql """
            CREATE TABLE t_jt_parse_101 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "SET enable_insert_strict=false"
        boolean threw = false
        try {
            sql "INSERT INTO t_jt_parse_101 VALUES (1,'{\"a\":1}'),(2,'{badbad'),(3,'{\"b\":2}')"
        } catch (Exception e) { threw = true }
        sql "SET enable_insert_strict=default"
        // lock observation; non-strict may skip or fail
        if (!threw) {
            def r = sql "SELECT count(*) FROM t_jt_parse_101"
            // expects 2 or 3 (skip-on-bad or NULL-on-bad)
            int cnt = Integer.parseInt(r[0][0].toString())
            assertTrue(cnt >= 2, "JT-PARSE-101: non-strict insert; cnt=${cnt}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_101" } catch (Exception ignore) {}
    }
}
