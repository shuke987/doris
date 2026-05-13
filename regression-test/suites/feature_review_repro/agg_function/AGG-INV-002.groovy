// AGG-INV-002: SUM 分组分解不变量
// Oracle: SUM(c) = SUM(c WHERE p) + SUM(c WHERE NOT p) (for disjoint partition)
// 当 p 与 NOT p 互斥且并集 = 全集时，分解成立
suite("repro_agg_inv_002") {
    sql "DROP TABLE IF EXISTS t_agg_inv_002"
    try {
        sql """
            CREATE TABLE t_agg_inv_002 (id INT, v INT, p INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_inv_002 VALUES
            (1, 10, 1),(2, 20, 1),(3, 30, 0),(4, 40, 0),(5, 50, 1),(6, NULL, 1),(7, 60, 0)"""
        def total = sql "SELECT SUM(v) FROM t_agg_inv_002"
        def part1 = sql "SELECT SUM(v) FROM t_agg_inv_002 WHERE p=1"
        def part2 = sql "SELECT SUM(v) FROM t_agg_inv_002 WHERE p=0"
        long sumTotal = (long)total[0][0]
        long sumP1 = (long)part1[0][0]
        long sumP2 = (long)part2[0][0]
        assertEquals(sumTotal, sumP1 + sumP2,
            "INVARIANT: SUM(c) = SUM(c WHERE p1) + SUM(c WHERE p0); total=${sumTotal} p1=${sumP1} p0=${sumP2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_002" } catch (Exception ignore) {}
    }
}
