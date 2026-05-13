// AGG-GB-002: GROUPING SETS 不变量
suite("repro_agg_gb_002") {
    sql "DROP TABLE IF EXISTS t_agg_gb_002"
    try {
        sql """
            CREATE TABLE t_agg_gb_002 (id INT, k1 INT, k2 VARCHAR(10), v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_gb_002 VALUES
            (1,1,'a',10),(2,1,'b',20),(3,2,'a',30),(4,2,'b',40)"""
        // GROUPING SETS ((k1), (k2), ()) 应得 3 个分组层级
        def r = sql "SELECT k1, k2, SUM(v) FROM t_agg_gb_002 GROUP BY GROUPING SETS ((k1), (k2), ())"
        // 3 个层级：2 (k1=1,2) + 2 (k2='a','b') + 1 (total) = 5 行
        assertEquals(5, r.size(), "GROUPING SETS ((k1),(k2),()) should produce 5 rows; got=${r.size()}")

        // total row: sum = 10+20+30+40 = 100
        boolean foundTotal = false
        for (def row : r) {
            if (row[0] == null && row[1] == null) {
                assertEquals(100L, row[2], "total sum = 100")
                foundTotal = true
            }
        }
        assertTrue(foundTotal, "GROUPING SETS should contain total row")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gb_002" } catch (Exception ignore) {}
    }
}
