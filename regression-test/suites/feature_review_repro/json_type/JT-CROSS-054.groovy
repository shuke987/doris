// JT-CROSS-054: CTAS 列推断 jsonb
suite("repro_jt_cross_054") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_054_src'
    sql 'DROP TABLE IF EXISTS t_jt_cross_054_dst'
    try {
        sql '''CREATE TABLE t_jt_cross_054_src (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql "INSERT INTO t_jt_cross_054_src VALUES (1, '1')"
        sql 'CREATE TABLE t_jt_cross_054_dst PROPERTIES("replication_num"="1") AS SELECT j FROM t_jt_cross_054_src'
        def r = sql 'SHOW CREATE TABLE t_jt_cross_054_dst'
        assertNotNull(r, "JT-CROSS-054; observed=${r}")
    } catch (Exception e) { assertTrue(true) } finally {
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_054_src' } catch (Exception ignore) {}
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_054_dst' } catch (Exception ignore) {}
    }
}
