// AGG-DT-007: MULTI_DISTINCT_SUM 与 SUM(DISTINCT) 等价
suite("repro_agg_dt_007") {
    sql "DROP TABLE IF EXISTS t_agg_dt_007"
    try {
        sql """CREATE TABLE t_agg_dt_007 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_dt_007 VALUES (1,10),(2,20),(3,10),(4,30),(5,20)"
        def r = sql "SELECT SUM(DISTINCT v), MULTI_DISTINCT_SUM(v) FROM t_agg_dt_007"
        assertEquals(r[0][0], r[0][1],
            "MULTI_DISTINCT_SUM ≡ SUM(DISTINCT); ${r[0][0]} vs ${r[0][1]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_007" } catch (Exception ignore) {}
    }
}
