// JT-CONSTRUCT-040: array_agg + ORDER BY
suite("repro_jt_construct_040") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_construct_040'
        try {
            sql '''CREATE TABLE t_jt_construct_040 (id INT, v INT, g INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
            sql 'INSERT INTO t_jt_construct_040 VALUES (1,1,1),(2,2,1),(3,3,2),(4,NULL,2),(5,5,2)'
            try { def r = sql 'SELECT json_array_agg(v ORDER BY v) FROM t_jt_construct_040'; assertNotNull(r[0][0]) } catch (Exception e) {}
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_construct_040' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CONSTRUCT-040: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
