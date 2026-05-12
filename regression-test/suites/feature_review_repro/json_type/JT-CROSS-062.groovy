// JT-CROSS-062: NULL jsonb 行跨函数 NULL map 处理一致
suite("repro_jt_cross_062") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_062'
    try {
        sql '''CREATE TABLE t_jt_cross_062 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cross_062 VALUES (1, NULL)'
        def r = sql '''SELECT json_extract(j, '$.a'), json_length(j), json_keys(j), json_type(j, '$') FROM t_jt_cross_062'''
        // all should return NULL (no query fail)
        assertEquals(1, r.size(), "JT-CROSS-062; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_062' } catch (Exception ignore) {} }
}
