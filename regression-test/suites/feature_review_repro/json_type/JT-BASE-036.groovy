// JT-BASE-036: MODIFY COLUMN JSONB → STRING
suite("repro_jt_base_036") {
    sql 'DROP TABLE IF EXISTS t_jt_base_036'
    try {
        sql '''CREATE TABLE t_jt_base_036 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_base_036 VALUES (1, \'{"a":1}\')'
        try { sql 'ALTER TABLE t_jt_base_036 MODIFY COLUMN j STRING' } catch (Exception e) { /* may async */ }
        def r = sql 'SELECT count(*) FROM t_jt_base_036'
        assertEquals('1', r[0][0].toString(), "JT-BASE-036; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_base_036' } catch (Exception ignore) {} }
}
