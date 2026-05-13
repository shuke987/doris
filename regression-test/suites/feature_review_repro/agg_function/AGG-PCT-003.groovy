// AGG-PCT-003: PERCENTILE 单调不变量: P(0.1) <= P(0.5) <= P(0.9)
suite("repro_agg_pct_003") {
    sql "DROP TABLE IF EXISTS t_agg_pct_003"
    try {
        sql """CREATE TABLE t_agg_pct_003 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_pct_003 VALUES (1,10),(2,20),(3,30),(4,40),(5,50),(6,60),(7,70),(8,80),(9,90)"
        def r = sql "SELECT PERCENTILE(v,0.1), PERCENTILE(v,0.5), PERCENTILE(v,0.9) FROM t_agg_pct_003"
        double p10 = (double)r[0][0]
        double p50 = (double)r[0][1]
        double p90 = (double)r[0][2]
        assertTrue(p10 <= p50, "P(0.1)=${p10} <= P(0.5)=${p50}")
        assertTrue(p50 <= p90, "P(0.5)=${p50} <= P(0.9)=${p90}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_pct_003" } catch (Exception ignore) {}
    }
}
