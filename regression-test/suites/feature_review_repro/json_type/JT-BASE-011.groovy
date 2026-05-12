// JT-BASE-011: UNIQUE KEY 含 JSONB 应拒绝
suite("repro_jt_base_011") {
    sql "DROP TABLE IF EXISTS t_jt_base_011"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_011 (id INT, j JSONB)
                UNIQUE KEY(j) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-011: JSONB cannot be UNIQUE key column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_011" } catch (Exception ignore) {}
    }
}
