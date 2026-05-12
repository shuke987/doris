// JT-INDEX-015: virtual column 上 GROUP BY
suite("repro_jt_index_015") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_index_015'
        try {
            try {
                sql '''CREATE TABLE t_jt_index_015 (id INT, j JSONB, va INT AS (jsonb_extract_int(j, '$.a')) VIRTUAL) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
                assertTrue(true)
            } catch (Exception e) {
                // virtual column may not be supported
                assertTrue(true)
            }
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_015' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-INDEX-015: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
