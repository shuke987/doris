// JT-INDEX-003: BITMAP index on JSONB 应拒绝
suite("repro_jt_index_003") {
    sql "DROP TABLE IF EXISTS t_jt_index_003"
    try {
        // syntax USING BITMAP not supported in nereids → already rejected
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_index_003 (id INT, j JSONB, INDEX idx_j(j) USING BITMAP)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-INDEX-003: BITMAP on JSONB should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_index_003" } catch (Exception ignore) {}
    }
}
