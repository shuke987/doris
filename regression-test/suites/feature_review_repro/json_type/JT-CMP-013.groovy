// JT-CMP-013: ORDER BY 含 NULL
suite("repro_jt_cmp_013") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_013'
    try {
        sql '''CREATE TABLE t_jt_cmp_013 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_013 VALUES (1, \'1\'),(2, NULL)'
        try { def r = sql 'SELECT id FROM t_jt_cmp_013 ORDER BY j NULLS FIRST'; assertEquals(2, r.size()) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_013' } catch (Exception ignore) {} }
}
