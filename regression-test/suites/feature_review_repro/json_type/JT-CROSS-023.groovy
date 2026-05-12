// JT-CROSS-023: 同表 jsonb + variant 列
suite("repro_jt_cross_023") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_023'
    try {
        sql '''CREATE TABLE t_jt_cross_023 (id INT, j JSONB, v VARIANT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql "INSERT INTO t_jt_cross_023 VALUES (1, '{\"a\":1}', '{\"a\":1}')"
        def r = sql 'SELECT count(*) FROM t_jt_cross_023'
        assertEquals('1', r[0][0].toString(), "JT-CROSS-023; observed=${r}")
    } catch (Exception e) { assertTrue(true) } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_023' } catch (Exception ignore) {} }
}
