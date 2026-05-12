// JT-BASE-007: JSONB DEFAULT 'invalid' 应拒绝
suite("repro_jt_base_007") {
    sql "DROP TABLE IF EXISTS t_jt_base_007"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_007 (id INT, j JSONB DEFAULT 'abc')
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-007: JSONB DEFAULT invalid string should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_007" } catch (Exception ignore) {}
    }
}
