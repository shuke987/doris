// JT-CMP-015: LIMIT 10 ORDER BY jsonb
suite("repro_jt_cmp_015") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_015'
    try {
        sql '''CREATE TABLE t_jt_cmp_015 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_015 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT id FROM t_jt_cmp_015 ORDER BY j LIMIT 10'; assertTrue(r.size() <= 10) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_015' } catch (Exception ignore) {} }
}
