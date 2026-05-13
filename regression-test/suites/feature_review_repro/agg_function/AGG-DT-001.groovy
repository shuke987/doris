// AGG-DT-001: COUNT(DISTINCT) baseline
// Oracle: hand-computed
suite("repro_agg_dt_001") {
    sql "DROP TABLE IF EXISTS t_agg_dt_001"
    try {
        sql """
            CREATE TABLE t_agg_dt_001 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_dt_001 VALUES (1,10),(2,10),(3,20),(4,20),(5,30),(6,NULL)"
        // DISTINCT {10,20,30} → 3 (NULL excluded)
        def r = sql "SELECT COUNT(DISTINCT v) FROM t_agg_dt_001"
        assertEquals(3L, r[0][0], "COUNT(DISTINCT v) = 3 unique non-null values")

        // approx_count_distinct 在小数据应等于 COUNT(DISTINCT)
        def r2 = sql "SELECT APPROX_COUNT_DISTINCT(v) FROM t_agg_dt_001"
        assertEquals(3L, r2[0][0],
            "APPROX_COUNT_DISTINCT on small data = COUNT(DISTINCT)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_001" } catch (Exception ignore) {}
    }
}
