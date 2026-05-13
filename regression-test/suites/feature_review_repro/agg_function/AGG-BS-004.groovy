// AGG-BS-004: SUM 类型自动 promote (TINYINT → BIGINT)
suite("repro_agg_bs_004") {
    sql "DROP TABLE IF EXISTS t_agg_bs_004"
    try {
        sql """
            CREATE TABLE t_agg_bs_004 (id INT, v TINYINT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // 三个 127（TINYINT MAX）求和 = 381，超 TINYINT range，应 promote 不溢出
        sql "INSERT INTO t_agg_bs_004 VALUES (1, 127),(2, 127),(3, 127)"
        def r = sql "SELECT SUM(v) FROM t_agg_bs_004"
        assertEquals(381L, r[0][0],
            "SUM(TINYINT) promotes to BIGINT to avoid overflow; 3×127=381")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bs_004" } catch (Exception ignore) {}
    }
}
