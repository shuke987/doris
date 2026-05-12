// JT-BASE-019: JSONB 不可作 LIST 分区列
suite("repro_jt_base_019") {
    sql "DROP TABLE IF EXISTS t_jt_base_019"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_019 (id INT, j JSONB)
                DUPLICATE KEY(id)
                PARTITION BY LIST(j) (PARTITION p1 VALUES IN ('{"a":1}'))
                DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-019: JSONB cannot be LIST partition column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_019" } catch (Exception ignore) {}
    }
}
