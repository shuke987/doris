// AGG-GB-001: GROUP BY + HAVING 不变量
// Oracle: 分组结果应满足 HAVING 谓词
suite("repro_agg_gb_001") {
    sql "DROP TABLE IF EXISTS t_agg_gb_001"
    try {
        sql """
            CREATE TABLE t_agg_gb_001 (id INT, k INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_gb_001 VALUES
            (1,1,10),(2,1,20),(3,2,5),(4,2,15),(5,3,100),(6,3,200)"""
        // GROUP BY k, HAVING SUM(v) > 100: k=2 → 20 (NO), k=1 → 30 (NO), k=3 → 300 (YES)
        def r = sql "SELECT k, SUM(v) FROM t_agg_gb_001 GROUP BY k HAVING SUM(v) > 100 ORDER BY k"
        assertEquals(1, r.size(), "HAVING SUM > 100 only k=3 qualifies")
        assertEquals(3, r[0][0], "k=3")
        assertEquals(300L, r[0][1], "SUM(v) for k=3 = 100+200=300")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gb_001" } catch (Exception ignore) {}
    }
}
