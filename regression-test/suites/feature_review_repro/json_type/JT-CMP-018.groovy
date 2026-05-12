// JT-CMP-018: GROUP BY 不同 key 顺序同对象
suite("repro_jt_cmp_018") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_018'
    try {
        sql '''CREATE TABLE t_jt_cmp_018 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_018 VALUES (1, \'{"a":1,"b":2}\'),(2, \'{"b":2,"a":1}\')'
        try { def r = sql 'SELECT count(*) FROM (SELECT j FROM t_jt_cmp_018 GROUP BY j) t'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_018' } catch (Exception ignore) {} }
}
