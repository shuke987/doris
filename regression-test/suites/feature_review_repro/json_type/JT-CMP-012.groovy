// JT-CMP-012: ORDER BY DESC
suite("repro_jt_cmp_012") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_012'
    try {
        sql '''CREATE TABLE t_jt_cmp_012 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_012 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT id FROM t_jt_cmp_012 ORDER BY j DESC'; assertEquals(2, r.size()) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_012' } catch (Exception ignore) {} }
}
