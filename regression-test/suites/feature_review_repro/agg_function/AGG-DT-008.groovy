// AGG-DT-008: MULTI_DISTINCT_COUNT 与 COUNT(DISTINCT) 等价
suite("repro_agg_dt_008") {
    sql "DROP TABLE IF EXISTS t_agg_dt_008"
    try {
        sql """CREATE TABLE t_agg_dt_008 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_dt_008 VALUES (1,10),(2,20),(3,10),(4,30),(5,NULL)"
        def r = sql "SELECT COUNT(DISTINCT v), MULTI_DISTINCT_COUNT(v) FROM t_agg_dt_008"
        assertEquals(r[0][0], r[0][1],
            "MULTI_DISTINCT_COUNT ≡ COUNT(DISTINCT); ${r[0][0]} vs ${r[0][1]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_008" } catch (Exception ignore) {}
    }
}
