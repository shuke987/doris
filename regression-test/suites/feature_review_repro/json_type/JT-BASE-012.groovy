// JT-BASE-012: AGGREGATE KEY 含 JSONB 应拒绝
suite("repro_jt_base_012") {
    sql "DROP TABLE IF EXISTS t_jt_base_012"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_012 (id INT, j JSONB)
                AGGREGATE KEY(j) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-012: JSONB cannot be AGGREGATE key column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_012" } catch (Exception ignore) {}
    }
}
