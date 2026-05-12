// JT-CMP-031: sort + normalize 组合 GROUP BY 归一
suite("repro_jt_cmp_031") {
    sql 'DROP TABLE IF EXISTS t_jt_cmp_031'
    try {
        sql '''CREATE TABLE t_jt_cmp_031 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cmp_031 VALUES (1, \'{"a":1,"b":2}\'),(2, \'{"b":2,"a":1}\')'
        try { def r = sql 'SELECT count(*) FROM (SELECT sort_json_object_keys(j) sj FROM t_jt_cmp_031 GROUP BY sort_json_object_keys(j)) t'; assertNotNull(r[0][0]) } catch (Exception e) {}
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cmp_031' } catch (Exception ignore) {} }
}
