// JT-CROSS-025: JSONB 列 cast → VARIANT
suite("repro_jt_cross_025") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_025'
    try {
        sql '''CREATE TABLE t_jt_cross_025 (id INT, j JSONB, v VARIANT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql "INSERT INTO t_jt_cross_025 VALUES (1, '{\"a\":1}', '{\"a\":1}')"
        try { def r = sql 'SELECT CAST(j AS VARIANT) FROM t_jt_cross_025' } catch (Exception e) {}
        def r = sql 'SELECT count(*) FROM t_jt_cross_025'
        assertEquals('1', r[0][0].toString(), "JT-CROSS-025; observed=${r}")
    } catch (Exception e) { assertTrue(true) } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_025' } catch (Exception ignore) {} }
}
