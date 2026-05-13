// AGG-PCT-002: PERCENTILE_APPROX 精度
suite("repro_agg_pct_002") {
    sql "DROP TABLE IF EXISTS t_agg_pct_002"
    try {
        sql """CREATE TABLE t_agg_pct_002 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        StringBuilder sb = new StringBuilder()
        for (int i = 1; i <= 100; i++) {
            if (i > 1) sb.append(",")
            sb.append("(${i}, ${i})")
        }
        sql "INSERT INTO t_agg_pct_002 VALUES ${sb.toString()}"
        // 100 个 1..100，精确中位数 = 50.5
        def r = sql "SELECT PERCENTILE_APPROX(v, 0.5), PERCENTILE(v, 0.5) FROM t_agg_pct_002"
        double approx = (double)r[0][0]
        double exact = (double)r[0][1]
        assertEquals(approx, exact, 5.0,
            "PERCENTILE_APPROX vs PERCENTILE within 5; approx=${approx} exact=${exact}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_pct_002" } catch (Exception ignore) {}
    }
}
