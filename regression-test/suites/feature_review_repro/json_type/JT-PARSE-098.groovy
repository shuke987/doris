// JT-PARSE-098: stream load equivalent — INSERT 非法行 strict
suite("repro_jt_parse_098") {
    sql "DROP TABLE IF EXISTS t_jt_parse_098"
    try {
        sql """
            CREATE TABLE t_jt_parse_098 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "SET enable_insert_strict=true"
        boolean threw = false
        try { sql "INSERT INTO t_jt_parse_098 VALUES (1,'{badbad')" }
        catch (Exception e) { threw = true }
        sql "SET enable_insert_strict=default"
        assertTrue(threw, "JT-PARSE-098: strict INSERT bad JSON should fail")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_098" } catch (Exception ignore) {}
    }
}
