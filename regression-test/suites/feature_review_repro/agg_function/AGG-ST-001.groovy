// AGG-ST-001: VARIANCE = E(x²) - E(x)² 数学不变量 (population variance)
// Oracle: 数学定义；浮点误差容忍 1e-6
suite("repro_agg_st_001") {
    sql "DROP TABLE IF EXISTS t_agg_st_001"
    try {
        sql """
            CREATE TABLE t_agg_st_001 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_st_001 VALUES (1, 10),(2, 20),(3, 30),(4, 40),(5, 50)"
        def r = sql "SELECT VARIANCE(v), AVG(v*v) - AVG(v)*AVG(v) FROM t_agg_st_001"
        double v1 = (double)r[0][0]
        double v2 = (double)r[0][1]
        assertEquals(v1, v2, 1e-6,
            "INVARIANT: VARIANCE = E(x²) - E(x)²; variance=${v1} manual=${v2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_st_001" } catch (Exception ignore) {}
    }
}
