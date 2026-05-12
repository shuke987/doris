// JT-SERDE-019: stream_load json 包含 BOM
suite("repro_jt_serde_019") {
    // stream_load not directly testable in suite — smoke probe via INSERT
    sql 'DROP TABLE IF EXISTS t_jt_serde_019'
    try {
        sql '''CREATE TABLE t_jt_serde_019 (id INT, j JSONB NULL) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_serde_019 VALUES (1, \'{"a":1}\'),(2, NULL)'
        def r = sql 'SELECT count(*) FROM t_jt_serde_019'
        assertEquals('2', r[0][0].toString(), "JT-SERDE-019; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_019' } catch (Exception ignore) {} }
}
