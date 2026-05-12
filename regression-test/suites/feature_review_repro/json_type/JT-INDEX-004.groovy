// JT-INDEX-004: NGRAM_BF on JSONB 应拒绝
suite("repro_jt_index_004") {
    sql "DROP TABLE IF EXISTS t_jt_index_004"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_index_004 (id INT, j JSONB, INDEX idx_j(j) USING NGRAM_BF PROPERTIES("gram_size"="3","bf_size"="256"))
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-INDEX-004: NGRAM_BF on JSONB should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_index_004" } catch (Exception ignore) {}
    }
}
