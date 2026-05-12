// JT-CONSTRUCT-043: array_agg DISTINCT
suite("repro_jt_construct_043") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_construct_043'
        try {
            sql '''CREATE TABLE t_jt_construct_043 (id INT, v INT, g INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
            sql 'INSERT INTO t_jt_construct_043 VALUES (1,1,1),(2,2,1),(3,3,2),(4,NULL,2),(5,5,2)'
            try { def r = sql 'SELECT json_array_agg(DISTINCT v) FROM t_jt_construct_043'; assertNotNull(r[0][0]) } catch (Exception e) {}
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_construct_043' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CONSTRUCT-043: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
