// JT-SERDE-010: format=json 单条 → JSONB 列
suite("repro_jt_serde_010") {
    sql 'DROP TABLE IF EXISTS t_jt_serde_010'
    try {
        sql '''CREATE TABLE t_jt_serde_010 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_serde_010 VALUES (1, \'{"a":1}\')'
        def r = sql 'SELECT j FROM t_jt_serde_010'
        String v = r[0][0].toString()
        assertTrue(v.contains('"a":1'), "JT-SERDE-010; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_010' } catch (Exception ignore) {} }
}
