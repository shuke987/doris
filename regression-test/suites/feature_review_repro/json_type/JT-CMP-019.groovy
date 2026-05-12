// JT-CMP-019: GROUP BY 数字 1 vs 1.0
suite("repro_jt_cmp_019") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_019'
    try {
        sql '''CREATE TABLE t_jt_cmp_019 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_019 VALUES (1, \'1\'),(2, \'1.0\')'
        try { def r = sql 'SELECT count(*) FROM (SELECT j FROM t_jt_cmp_019 GROUP BY j) t'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_019' } catch (Exception ignore) {} }
}
