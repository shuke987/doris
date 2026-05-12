// JT-INDEX-007: inverted index 删除 / rebuild on jsonb
suite("repro_jt_index_007") {
    sql 'DROP TABLE IF EXISTS t_jt_index_007'
    try {
        boolean threw=false
        try { sql '''CREATE TABLE t_jt_index_007 (id INT, j JSONB, INDEX idx_j(j) USING INVERTED) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")''' } catch (Exception e) { threw=true }
        // inverted on jsonb should already be rejected — see JT-INDEX-001
        assertTrue(threw, "JT-INDEX-007: inverted index on JSONB should be rejected")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_007' } catch (Exception ignore) {} }
}
