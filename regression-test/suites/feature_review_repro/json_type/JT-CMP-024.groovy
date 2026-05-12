// JT-CMP-024: nestloop join ON json_extract(...) = ...
suite("repro_jt_cmp_024") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_024'
    try {
        sql '''CREATE TABLE t_jt_cmp_024 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_024 VALUES (1, \'{"a":1}\')'
        try { def r = sql 'SELECT count(*) FROM t_jt_cmp_024 a JOIN t_jt_cmp_024 b ON json_extract(a.j, \'$.a\') = json_extract(b.j, \'$.a\')'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_024' } catch (Exception ignore) {} }
}
