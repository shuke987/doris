// JT-MODIFY-065: const jsonb data + 变化 value 多行
suite("repro_jt_modify_065") {
    sql 'DROP TABLE IF EXISTS t_jt_modify_065'
    try {
        sql '''CREATE TABLE t_jt_modify_065 (id INT, v INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_modify_065 VALUES (1,1),(2,2),(3,3)'
        def r = sql 'SELECT json_set(\'{"a":1}\', \'$.b\', v) FROM t_jt_modify_065 ORDER BY id'
        assertEquals(3, r.size(), "JT-MODIFY-065; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_modify_065' } catch (Exception ignore) {} }
}
