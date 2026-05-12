// JT-SERDE-011: format=json array `[{},{}]`
suite("repro_jt_serde_011") {
    sql 'DROP TABLE IF EXISTS t_jt_serde_011'
    try {
        sql '''CREATE TABLE t_jt_serde_011 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_serde_011 VALUES (1, \'{"a":1}\')'
        def r = sql 'SELECT j FROM t_jt_serde_011'
        String v = r[0][0].toString()
        assertTrue(v.contains('"a":1'), "JT-SERDE-011; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_011' } catch (Exception ignore) {} }
}
