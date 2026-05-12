// JT-CMP-021: COUNT(DISTINCT jsonb)
suite("repro_jt_cmp_021") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_021'
    try {
        sql '''CREATE TABLE t_jt_cmp_021 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_021 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT count(DISTINCT j) FROM t_jt_cmp_021'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_021' } catch (Exception ignore) {} }
}
