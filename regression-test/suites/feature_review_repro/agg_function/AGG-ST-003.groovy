// AGG-ST-003: STDDEV_POP vs STDDEV_SAMP 区分
// Oracle: 数学定义
//   POP (population): SQRT(sum((x-avg)^2) / N)
//   SAMP (sample): SQRT(sum((x-avg)^2) / (N-1))
//   SAMP > POP for N>1
suite("repro_agg_st_003") {
    sql "DROP TABLE IF EXISTS t_agg_st_003"
    try {
        sql """CREATE TABLE t_agg_st_003 (id INT, v DOUBLE) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_st_003 VALUES (1, 10),(2, 20),(3, 30),(4, 40),(5, 50)"
        def r = sql "SELECT STDDEV_POP(v), STDDEV_SAMP(v), VAR_POP(v), VAR_SAMP(v) FROM t_agg_st_003"
        double pop = (double)r[0][0]
        double samp = (double)r[0][1]
        double varPop = (double)r[0][2]
        double varSamp = (double)r[0][3]

        assertTrue(samp > pop,
            "STDDEV_SAMP > STDDEV_POP for N>1; pop=${pop} samp=${samp}")
        // VAR_POP = STDDEV_POP^2
        assertEquals(varPop, pop * pop, 1e-6, "VAR_POP = STDDEV_POP^2; var=${varPop} pop^2=${pop*pop}")
        // VAR_SAMP = STDDEV_SAMP^2
        assertEquals(varSamp, samp * samp, 1e-6, "VAR_SAMP = STDDEV_SAMP^2")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_st_003" } catch (Exception ignore) {}
    }
}
