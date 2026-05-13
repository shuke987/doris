// AGG-HLL-001: HLL_CARDINALITY + HLL_HASH 数学性质
suite("repro_agg_hll_001") {
    sql "DROP TABLE IF EXISTS t_agg_hll_001"
    try {
        sql """CREATE TABLE t_agg_hll_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_hll_001 VALUES
            (1,1),(2,2),(3,3),(4,1),(5,2)"""
        // HLL_UNION_AGG 小数据应 = COUNT(DISTINCT) = 3
        def r = sql "SELECT HLL_UNION_AGG(HLL_HASH(v)), COUNT(DISTINCT v) FROM t_agg_hll_001"
        long hll = (long)r[0][0]
        long dist = (long)r[0][1]
        assertEquals(dist, hll, "HLL on small data ≈ COUNT(DISTINCT); hll=${hll} distinct=${dist}")
        assertEquals(3L, dist, "3 distinct values {1,2,3}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_hll_001" } catch (Exception ignore) {}
    }
}
