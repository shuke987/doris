// AGG-CTE-001: agg in CTE
suite("repro_agg_cte_001") {
    sql "DROP TABLE IF EXISTS t_agg_cte_001"
    try {
        sql """CREATE TABLE t_agg_cte_001 (id INT, k INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_cte_001 VALUES
            (1,1,10),(2,1,20),(3,2,30)"""
        def r = sql """
            WITH agg_cte AS (SELECT k, SUM(v) AS s FROM t_agg_cte_001 GROUP BY k)
            SELECT SUM(s) FROM agg_cte
        """
        // sum = 30 + 30 = 60
        assertEquals(60L, r[0][0], "agg in CTE: SUM(SUM(v) GROUP BY k) = 60")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_cte_001" } catch (Exception ignore) {}
    }
}
