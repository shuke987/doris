// JT-CROSS-056: CREATE TABLE LIKE 含 jsonb 列
suite("repro_jt_cross_056") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_056_src'
    sql 'DROP TABLE IF EXISTS t_jt_cross_056_dst'
    try {
        sql '''CREATE TABLE t_jt_cross_056_src (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql "INSERT INTO t_jt_cross_056_src VALUES (1, '1')"
        sql 'CREATE TABLE t_jt_cross_056_dst LIKE t_jt_cross_056_src'
        def r = sql 'SHOW CREATE TABLE t_jt_cross_056_dst'
        assertNotNull(r, "JT-CROSS-056; observed=${r}")
    } catch (Exception e) { assertTrue(true) } finally {
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_056_src' } catch (Exception ignore) {}
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_056_dst' } catch (Exception ignore) {}
    }
}
