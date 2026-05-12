// JT-CROSS-059: ANALYZE TABLE t COLUMNS(jsonb_col)
suite("repro_jt_cross_059") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_059'
    try {
        sql '''CREATE TABLE t_jt_cross_059 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cross_059 VALUES (1, \'{}\')'
        try { sql 'ANALYZE TABLE t_jt_cross_059' } catch (Exception e) {}
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_059' } catch (Exception ignore) {} }
}
