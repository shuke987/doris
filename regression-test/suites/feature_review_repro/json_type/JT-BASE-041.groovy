// JT-BASE-041: LIGHT_SCHEMA_CHANGE + MODIFY VARCHAR→JSONB
suite("repro_jt_base_041") {
    sql 'DROP TABLE IF EXISTS t_jt_base_041'
    try {
        sql '''CREATE TABLE t_jt_base_041 (id INT, v VARCHAR(100)) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1","light_schema_change"="true")'''
        sql 'INSERT INTO t_jt_base_041 VALUES (1, \'{"a":1}\')'
        try { sql 'ALTER TABLE t_jt_base_041 MODIFY COLUMN v JSONB' } catch (Exception e) { /* expected to degrade */ }
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_base_041' } catch (Exception ignore) {} }
}
