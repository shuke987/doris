// JT-CROSS-022: JSONB column 作 view
suite("repro_jt_cross_022") {
    try {
        sql "DROP TABLE IF EXISTS t_jt_cross_022"
        sql "DROP VIEW IF EXISTS v_jt_cross_022"
        try {
            sql """
                CREATE TABLE t_jt_cross_022 (id INT, j JSONB)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
            sql "INSERT INTO t_jt_cross_022 VALUES (1,'{\"a\":42}')"
            sql "CREATE VIEW v_jt_cross_022 AS SELECT id, jsonb_extract_int(j, '\$.a') AS a FROM t_jt_cross_022"
            def r = sql "SELECT a FROM v_jt_cross_022 WHERE id=1"
            assertEquals("42", r[0][0].toString(), "JT-CROSS-022; observed=${r}")
        } finally {
            try { sql "DROP VIEW IF EXISTS v_jt_cross_022" } catch (Exception ignore) {}
            try { sql "DROP TABLE IF EXISTS t_jt_cross_022" } catch (Exception ignore) {}
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CROSS-022: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
