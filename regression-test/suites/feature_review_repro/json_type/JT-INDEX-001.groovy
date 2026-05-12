// JT-INDEX-001: INVERTED index on JSONB 应拒绝
suite("repro_jt_index_001") {
    sql "DROP TABLE IF EXISTS t_jt_index_001"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_index_001 (id INT, j JSONB, INDEX idx_j(j) USING INVERTED)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-INDEX-001: INVERTED on JSONB should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_index_001" } catch (Exception ignore) {}
    }
}
