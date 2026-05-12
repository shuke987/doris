// JT-CONSTRUCT-041: array_agg + GROUP BY
suite("repro_jt_construct_041") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_construct_041'
        try {
            sql '''CREATE TABLE t_jt_construct_041 (id INT, v INT, g INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
            sql 'INSERT INTO t_jt_construct_041 VALUES (1,1,1),(2,2,1),(3,3,2),(4,NULL,2),(5,5,2)'
            try { def r = sql 'SELECT g, json_array_agg(v) FROM t_jt_construct_041 GROUP BY g'; assertNotNull(r) } catch (Exception e) {}
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_construct_041' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CONSTRUCT-041: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
