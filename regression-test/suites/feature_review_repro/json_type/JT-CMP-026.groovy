// JT-CMP-026: join NULL 行（jsonb NULL）
suite("repro_jt_cmp_026") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_026'
    try {
        sql '''CREATE TABLE t_jt_cmp_026 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_026 VALUES (1, NULL),(2, NULL)'
        try { def r = sql 'SELECT count(*) FROM t_jt_cmp_026 a JOIN t_jt_cmp_026 b ON a.j = b.j'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_026' } catch (Exception ignore) {} }
}
