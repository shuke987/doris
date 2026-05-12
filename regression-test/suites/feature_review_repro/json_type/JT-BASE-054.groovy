// JT-BASE-054: `IS NOT NULL` 对 JSON null 行
suite("repro_jt_base_054") {
    sql 'DROP TABLE IF EXISTS t_jt_base_054'
    try {
        sql '''CREATE TABLE t_jt_base_054 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_base_054 VALUES (1, \'null\')'
        def r = sql 'SELECT count(*) FROM t_jt_base_054 WHERE j IS NOT NULL'
        assertEquals('1', r[0][0].toString(), "JT-BASE-054: JSON null row should hit IS NOT NULL; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_base_054' } catch (Exception ignore) {} }
}
