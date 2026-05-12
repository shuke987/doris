// JT-INDEX-002: BLOOM FILTER on JSONB 应拒绝
suite("repro_jt_index_002") {
    sql "DROP TABLE IF EXISTS t_jt_index_002"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_index_002 (id INT, j JSONB)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1","bloom_filter_columns"="j")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-INDEX-002: bloom filter on JSONB should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_index_002" } catch (Exception ignore) {}
    }
}
