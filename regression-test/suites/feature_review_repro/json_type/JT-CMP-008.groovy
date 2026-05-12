// JT-CMP-008: WHERE jsonb_extract_int(j, '$.a') > N
suite("repro_jt_cmp_008") {
    try {
        sql "DROP TABLE IF EXISTS t_jt_cmp_008"
        try {
            sql """
                CREATE TABLE t_jt_cmp_008 (id INT, j JSONB)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
            sql "INSERT INTO t_jt_cmp_008 VALUES (1,'{\"a\":1}'),(2,'{\"a\":5}'),(3,'{\"a\":10}')"
            def r = sql "SELECT count(*) FROM t_jt_cmp_008 WHERE jsonb_extract_int(j, '\$.a') > 2"
            assertEquals("2", r[0][0].toString(), "JT-CMP-008; observed=${r}")
        } finally {
            try { sql "DROP TABLE IF EXISTS t_jt_cmp_008" } catch (Exception ignore) {}
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CMP-008: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
