// JT-BASE-053: IS NULL 对 JSON null 行不命中
suite("repro_jt_base_053") {
    sql "DROP TABLE IF EXISTS t_jt_base_053"
    try {
        sql """
            CREATE TABLE t_jt_base_053 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_053 VALUES (1, 'null'), (2, '{\"a\":1}')"
        def r = sql "SELECT id FROM t_jt_base_053 WHERE j IS NULL ORDER BY id"
        assertEquals(0, r.size(),
            "JT-BASE-053: JSON null row should NOT hit IS NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_053" } catch (Exception ignore) {}
    }
}
