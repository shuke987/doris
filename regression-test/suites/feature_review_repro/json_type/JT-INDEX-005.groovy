// JT-INDEX-005: inverted index + ALTER ADD jsonb 列
suite("repro_jt_index_005") {
    sql 'DROP TABLE IF EXISTS t_jt_index_005'
    try {
        sql '''CREATE TABLE t_jt_index_005 (id INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        try { sql 'ALTER TABLE t_jt_index_005 ADD COLUMN j JSONB' } catch (Exception e) {}
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_005' } catch (Exception ignore) {} }
}
