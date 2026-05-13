// AGG-BND-004: agg 与 LIMIT 交互
suite("repro_agg_bnd_004") {
    sql "DROP TABLE IF EXISTS t_agg_bnd_004"
    try {
        sql """CREATE TABLE t_agg_bnd_004 (id INT, k INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_bnd_004 VALUES
            (1,1,10),(2,1,20),(3,2,30),(4,3,40)"""
        // GROUP BY k LIMIT 应只返 LIMIT 行
        def r = sql "SELECT k, SUM(v) FROM t_agg_bnd_004 GROUP BY k ORDER BY k LIMIT 2"
        assertEquals(2, r.size(), "GROUP BY LIMIT 2 should return 2 rows; got=${r.size()}")
        assertEquals(1, r[0][0], "first k after sort = 1")
        assertEquals(2, r[1][0], "second k = 2")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bnd_004" } catch (Exception ignore) {}
    }
}
