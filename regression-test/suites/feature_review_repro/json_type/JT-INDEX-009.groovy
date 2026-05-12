// JT-INDEX-009: PROPERTIES bloom_filter_columns 含 jsonb
suite("repro_jt_index_009") {
    sql 'DROP TABLE IF EXISTS t_jt_index_009'
    try {
        boolean threw=false
        try { sql '''CREATE TABLE t_jt_index_009 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1","bloom_filter_columns"="j")''' } catch (Exception e) { threw=true }
        assertTrue(threw, "JT-INDEX-009: bloom on JSONB should be rejected")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_009' } catch (Exception ignore) {} }
}
