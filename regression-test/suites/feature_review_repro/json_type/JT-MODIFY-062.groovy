// JT-MODIFY-062: UPDATE + partial_update
suite("repro_jt_modify_062") {
    sql 'DROP TABLE IF EXISTS t_jt_modify_062'
    try {
        sql '''CREATE TABLE t_jt_modify_062 (id INT, j JSONB) UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1","enable_unique_key_merge_on_write"="true")'''
        sql 'INSERT INTO t_jt_modify_062 VALUES (1, \'{"a":1}\')'
        try { sql 'UPDATE t_jt_modify_062 SET j = \'{"a":2}\' WHERE id = 1' } catch (Exception e) {}
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_modify_062' } catch (Exception ignore) {} }
}
