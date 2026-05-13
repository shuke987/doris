// AGG-INV-003: MAX >= MIN 数学不变量
suite("repro_agg_inv_003") {
    sql "DROP TABLE IF EXISTS t_agg_inv_003"
    try {
        sql """
            CREATE TABLE t_agg_inv_003 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_inv_003 VALUES (1, -100),(2, 50),(3, 0),(4, NULL),(5, 200)"
        def r = sql "SELECT MIN(v), MAX(v) FROM t_agg_inv_003"
        int minV = (int)r[0][0]
        int maxV = (int)r[0][1]
        assertTrue(maxV >= minV,
            "INVARIANT: MAX(${maxV}) >= MIN(${minV})")
        assertEquals(-100, minV, "MIN ignores NULL")
        assertEquals(200, maxV, "MAX ignores NULL")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_003" } catch (Exception ignore) {}
    }
}
