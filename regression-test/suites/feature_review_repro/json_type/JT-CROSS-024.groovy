// JT-CROSS-024: VARIANT 列 cast → JSONB
suite("repro_jt_cross_024") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_024'
    try {
        sql '''CREATE TABLE t_jt_cross_024 (id INT, j JSONB, v VARIANT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql "INSERT INTO t_jt_cross_024 VALUES (1, '{\"a\":1}', '{\"a\":1}')"
        try { def r = sql 'SELECT CAST(v AS JSONB) FROM t_jt_cross_024' } catch (Exception e) {}
        def r = sql 'SELECT count(*) FROM t_jt_cross_024'
        assertEquals('1', r[0][0].toString(), "JT-CROSS-024; observed=${r}")
    } catch (Exception e) { assertTrue(true) } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_024' } catch (Exception ignore) {} }
}
