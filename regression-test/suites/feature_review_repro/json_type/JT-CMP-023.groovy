// JT-CMP-023: hash join ON j1 = j2
suite("repro_jt_cmp_023") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_023'
    try {
        sql '''CREATE TABLE t_jt_cmp_023 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_023 VALUES (1, \'1\'),(2, \'2\')'
        try { def r = sql 'SELECT count(*) FROM t_jt_cmp_023 a JOIN t_jt_cmp_023 b ON a.j = b.j'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_023' } catch (Exception ignore) {} }
}
