// AGG-GB-003: ROLLUP 不变量
suite("repro_agg_gb_003") {
    sql "DROP TABLE IF EXISTS t_agg_gb_003"
    try {
        sql """
            CREATE TABLE t_agg_gb_003 (id INT, k1 INT, k2 VARCHAR(10), v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_gb_003 VALUES
            (1,1,'a',10),(2,1,'b',20),(3,2,'a',30)"""
        def r = sql "SELECT k1, k2, SUM(v) FROM t_agg_gb_003 GROUP BY ROLLUP(k1, k2)"
        // ROLLUP(k1,k2) = (k1,k2) UNION (k1) UNION ()
        // (1,a)=10, (1,b)=20, (2,a)=30, (1,*)=30, (2,*)=30, (*,*)=60 → 6 rows
        assertEquals(6, r.size(), "ROLLUP(k1,k2) should produce 6 rows; got=${r.size()}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gb_003" } catch (Exception ignore) {}
    }
}
