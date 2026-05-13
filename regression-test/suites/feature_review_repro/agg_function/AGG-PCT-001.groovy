// AGG-PCT-001: PERCENTILE 边界 0.0/1.0/0.5
// Oracle: PERCENTILE(c, 0.0)=MIN, PERCENTILE(c, 1.0)=MAX, PERCENTILE(c, 0.5)=median
suite("repro_agg_pct_001") {
    sql "DROP TABLE IF EXISTS t_agg_pct_001"
    try {
        sql """CREATE TABLE t_agg_pct_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_pct_001 VALUES (1,10),(2,20),(3,30),(4,40),(5,50)"
        def r = sql "SELECT PERCENTILE(v,0.0), PERCENTILE(v,1.0), PERCENTILE(v,0.5), MIN(v), MAX(v) FROM t_agg_pct_001"
        // PERCENTILE returns DOUBLE
        assertEquals(10.0, (double)r[0][0], 1e-9, "PERCENTILE(0.0) = MIN")
        assertEquals(50.0, (double)r[0][1], 1e-9, "PERCENTILE(1.0) = MAX")
        assertEquals(30.0, (double)r[0][2], 1e-9, "PERCENTILE(0.5) = median")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_pct_001" } catch (Exception ignore) {}
    }
}
