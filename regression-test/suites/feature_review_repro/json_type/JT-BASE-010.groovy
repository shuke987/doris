// JT-BASE-010: DUPLICATE KEY 含 JSONB 应拒绝
suite("repro_jt_base_010") {
    sql "DROP TABLE IF EXISTS t_jt_base_010"
    try {
        boolean threw = false; String err = ""
        try {
            sql """
                CREATE TABLE t_jt_base_010 (id INT, j JSONB)
                DUPLICATE KEY(j) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true; err = e.message ?: "" }
        assertTrue(threw,
            "JT-BASE-010: JSONB cannot be DUPLICATE key column; observed no error")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_010" } catch (Exception ignore) {}
    }
}
