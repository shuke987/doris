// JT-BASE-008: JSONB 列名大小写敏感性
suite("repro_jt_base_008") {
    sql "DROP TABLE IF EXISTS t_jt_base_008"
    try {
        // Doris column names are case-insensitive by default
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_008 (id INT, j JSONB, J JSONB)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-008: dup col name j vs J should be rejected (case-insens)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_008" } catch (Exception ignore) {}
    }
}
