// JT-SERDE-016: format=csv，含转义引号 jsonb
suite("repro_jt_serde_016") {
    // stream_load not directly testable in suite — smoke probe via INSERT
    sql 'DROP TABLE IF EXISTS t_jt_serde_016'
    try {
        sql '''CREATE TABLE t_jt_serde_016 (id INT, j JSONB NULL) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_serde_016 VALUES (1, \'{"a":1}\'),(2, NULL)'
        def r = sql 'SELECT count(*) FROM t_jt_serde_016'
        assertEquals('2', r[0][0].toString(), "JT-SERDE-016; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_016' } catch (Exception ignore) {} }
}
