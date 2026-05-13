// AGG-ST-002: STDDEV = sqrt(VARIANCE) 不变量
suite("repro_agg_st_002") {
    sql "DROP TABLE IF EXISTS t_agg_st_002"
    try {
        sql """
            CREATE TABLE t_agg_st_002 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_st_002 VALUES (1, 10),(2, 20),(3, 30),(4, 40),(5, 50)"
        def r = sql "SELECT STDDEV(v), SQRT(VARIANCE(v)) FROM t_agg_st_002"
        double s = (double)r[0][0]
        double sqrtv = (double)r[0][1]
        assertEquals(s, sqrtv, 1e-6,
            "INVARIANT: STDDEV = SQRT(VARIANCE); stddev=${s} sqrt(var)=${sqrtv}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_st_002" } catch (Exception ignore) {}
    }
}
