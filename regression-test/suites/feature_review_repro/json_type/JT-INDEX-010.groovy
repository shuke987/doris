// JT-INDEX-010: ALTER 添加 bloom on jsonb
suite("repro_jt_index_010") {
    sql 'DROP TABLE IF EXISTS t_jt_index_010'
    try {
        sql '''CREATE TABLE t_jt_index_010 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        boolean threw=false
        try { sql 'ALTER TABLE t_jt_index_010 SET ("bloom_filter_columns"="j")' } catch (Exception e) { threw=true }
        assertTrue(threw, "JT-INDEX-010: ALTER add bloom on JSONB should be rejected")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_010' } catch (Exception ignore) {} }
}
