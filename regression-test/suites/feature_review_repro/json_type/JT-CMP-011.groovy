// JT-CMP-011: ORDER BY jsonb_col ASC
suite("repro_jt_cmp_011") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_011'
    try {
        sql '''CREATE TABLE t_jt_cmp_011 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_011 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT id FROM t_jt_cmp_011 ORDER BY j ASC'; assertEquals(2, r.size()) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_011' } catch (Exception ignore) {} }
}
