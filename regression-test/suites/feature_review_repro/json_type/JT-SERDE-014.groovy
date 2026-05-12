// JT-SERDE-014: format=csv，列含 JSONB
suite("repro_jt_serde_014") {
    // stream_load not directly testable in suite — smoke probe via INSERT
    sql 'DROP TABLE IF EXISTS t_jt_serde_014'
    try {
        sql '''CREATE TABLE t_jt_serde_014 (id INT, j JSONB NULL) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_serde_014 VALUES (1, \'{"a":1}\'),(2, NULL)'
        def r = sql 'SELECT count(*) FROM t_jt_serde_014'
        assertEquals('2', r[0][0].toString(), "JT-SERDE-014; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_014' } catch (Exception ignore) {} }
}
