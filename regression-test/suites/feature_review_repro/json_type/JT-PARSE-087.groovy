// JT-PARSE-087: vector + default 列
suite("repro_jt_parse_087") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_parse_087'
        try {
            sql '''CREATE TABLE t_jt_parse_087 (id INT, s STRING, d STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
            sql 'INSERT INTO t_jt_parse_087 VALUES (1,\'bad\', \'{\"x\":1}\'),(2,\'{}\',\'{}\')'
            def r = sql 'SELECT jsonb_parse_error_to_value(s,d) FROM t_jt_parse_087 ORDER BY id'
            assertEquals(2, r.size(), "JT-PARSE-087; observed=${r}")
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_parse_087' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-087: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
