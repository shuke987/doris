// JT-BASE-021: JSONB 不可作 HASH bucket 列
suite("repro_jt_base_021") {
    sql "DROP TABLE IF EXISTS t_jt_base_021"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_021 (id INT, j JSONB)
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(j) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-021: JSONB cannot be HASH bucket column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_021" } catch (Exception ignore) {}
    }
}
