// JT-BASE-016: AGGREGATE + JSONB MAX 应拒绝
suite("repro_jt_base_016") {
    sql "DROP TABLE IF EXISTS t_jt_base_016"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_016 (id INT, j JSONB MAX)
                AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-016: JSONB MAX agg should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_016" } catch (Exception ignore) {}
    }
}
