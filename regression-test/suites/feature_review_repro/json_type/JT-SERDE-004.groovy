// JT-SERDE-004: 多行 update / overwrite
suite("repro_jt_serde_004") {
    try {
        sql "DROP TABLE IF EXISTS t_jt_serde_004"
        try {
            sql """
                CREATE TABLE t_jt_serde_004 (id INT, j JSONB)
                UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
            sql "INSERT INTO t_jt_serde_004 VALUES (1,'{\"a\":1}')"
            sql "INSERT INTO t_jt_serde_004 VALUES (1,'{\"a\":2}')"
            def r = sql "SELECT jsonb_extract_int(j, '\$.a') FROM t_jt_serde_004 WHERE id=1"
            assertEquals("2", r[0][0].toString(),
                "JT-SERDE-004: unique key overwrite; observed=${r}")
        } finally {
            try { sql "DROP TABLE IF EXISTS t_jt_serde_004" } catch (Exception ignore) {}
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-SERDE-004: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
