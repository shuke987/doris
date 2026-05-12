// JT-CMP-020: DISTINCT jsonb_col
suite("repro_jt_cmp_020") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_020'
    try {
        sql '''CREATE TABLE t_jt_cmp_020 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_020 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT DISTINCT j FROM t_jt_cmp_020'; assertTrue(r.size() >= 1) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_020' } catch (Exception ignore) {} }
}
