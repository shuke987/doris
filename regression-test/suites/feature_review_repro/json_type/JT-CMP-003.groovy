// JT-CMP-003: ORDER BY JSONB 应工作（binary sort）
suite("repro_jt_cmp_003") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_003"
    try {
        sql """
            CREATE TABLE t_jt_cmp_003 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_003 VALUES (1,'3'),(2,'1'),(3,'2')"
        def r = sql "SELECT id FROM t_jt_cmp_003 ORDER BY j"
        assertEquals(3, r.size(), "JT-CMP-003: 3 rows; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_003" } catch (Exception ignore) {}
    }
}
