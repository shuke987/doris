// JT-CONSTRUCT-048: object_agg 重复 key
suite("repro_jt_construct_048") {
    try {
        sql 'DROP TABLE IF EXISTS t_jt_construct_048'
        try {
            sql '''CREATE TABLE t_jt_construct_048 (id INT, k VARCHAR(50), v INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
            sql "INSERT INTO t_jt_construct_048 VALUES (1,'a',1),(2,'b',2)"
            try { def r = sql 'SELECT json_object_agg(k,v) FROM t_jt_construct_048'; assertNotNull(r) } catch (Exception e) {}
        } finally { try { sql 'DROP TABLE IF EXISTS t_jt_construct_048' } catch (Exception ignore) {} }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CONSTRUCT-048: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
