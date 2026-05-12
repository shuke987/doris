// JT-BASE-018: JSONB 不可作 RANGE 分区列
suite("repro_jt_base_018") {
    sql "DROP TABLE IF EXISTS t_jt_base_018"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_018 (id INT, j JSONB)
                DUPLICATE KEY(id)
                PARTITION BY RANGE(j) (PARTITION p1 VALUES LESS THAN ('{"a":1}'))
                DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-018: JSONB cannot be RANGE partition column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_018" } catch (Exception ignore) {}
    }
}
