// JT-PARSE-065: vector + 错误行 FAIL mode → query fail
suite("repro_jt_parse_065") {
    sql "DROP TABLE IF EXISTS t_jt_parse_065"
    try {
        sql """
            CREATE TABLE t_jt_parse_065 (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def vals = (1..10).collect {
            int x = it
            return (x == 5) ? "(5,'{badbad')" : "(${x},'{\"a\":${x}}')"
        }.join(",")
        sql "INSERT INTO t_jt_parse_065 VALUES ${vals}"
        boolean threw = false
        try { sql "SELECT jsonb_parse(s) FROM t_jt_parse_065" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-PARSE-065: 1 bad row → FAIL mode throws")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_065" } catch (Exception ignore) {}
    }
}
