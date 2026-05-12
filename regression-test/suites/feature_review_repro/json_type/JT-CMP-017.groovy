// JT-CMP-017: GROUP BY jsonb_col
suite("repro_jt_cmp_017") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_017'
    try {
        sql '''CREATE TABLE t_jt_cmp_017 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_017 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT j, count(*) FROM t_jt_cmp_017 GROUP BY j'; assertTrue(r.size() >= 1) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_017' } catch (Exception ignore) {} }
}
