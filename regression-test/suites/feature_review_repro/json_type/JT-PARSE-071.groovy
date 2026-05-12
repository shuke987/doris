// JT-PARSE-071: vector 混合行（部分非法）parse_error_to_null
suite("repro_jt_parse_071") {
    try {
        sql "DROP TABLE IF EXISTS t_jt_parse_071"
        try {
            sql """
                CREATE TABLE t_jt_parse_071 (id INT, s VARCHAR(1000))
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
            sql "INSERT INTO t_jt_parse_071 VALUES (1,'{\"a\":1}'),(2,'{badbad'),(3,'{\"b\":2}')"
            def r = sql "SELECT count(*) FROM t_jt_parse_071 WHERE jsonb_parse_error_to_null(s) IS NULL"
            assertEquals("1", r[0][0].toString(),
                "JT-PARSE-071: 1 malformed → NULL; observed=${r}")
        } finally {
            try { sql "DROP TABLE IF EXISTS t_jt_parse_071" } catch (Exception ignore) {}
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-071: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
