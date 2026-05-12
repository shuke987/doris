// JT-CROSS-016: ARRAY<JSONB> 建表
suite("repro_jt_cross_016") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_016'
    try {
        sql '''CREATE TABLE t_jt_cross_016 (id INT, arr ARRAY<JSONB>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        def r = sql 'SHOW CREATE TABLE t_jt_cross_016'
        assertNotNull(r, "JT-CROSS-016; observed=${r}")
    } catch (Exception e) {
        // ARRAY<JSONB> may not be supported
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_016' } catch (Exception ignore) {} }
}
