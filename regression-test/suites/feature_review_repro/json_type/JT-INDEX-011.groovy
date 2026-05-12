// JT-INDEX-011: CREATE INDEX USING BITMAP on jsonb
suite("repro_jt_index_011") {
    sql 'DROP TABLE IF EXISTS t_jt_index_011'
    try {
        sql '''CREATE TABLE t_jt_index_011 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        boolean threw=false
        try { sql 'CREATE INDEX idx_j ON t_jt_index_011 (j) USING BITMAP' } catch (Exception e) { threw=true }
        assertTrue(threw, "JT-INDEX-011: BITMAP on JSONB should be rejected")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_011' } catch (Exception ignore) {} }
}
