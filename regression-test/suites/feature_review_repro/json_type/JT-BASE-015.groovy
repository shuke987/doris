// JT-BASE-015: AGGREGATE + JSONB SUM 应拒绝
suite("repro_jt_base_015") {
    sql "DROP TABLE IF EXISTS t_jt_base_015"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_015 (id INT, j JSONB SUM)
                AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-015: JSONB SUM agg should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_015" } catch (Exception ignore) {}
    }
}
