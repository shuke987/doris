// AGG-COMB-002: HLL_UNION_AGG
suite("repro_agg_comb_002") {
    sql "DROP TABLE IF EXISTS t_agg_comb_002"
    try {
        sql """CREATE TABLE t_agg_comb_002 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_comb_002 VALUES
            (1,1),(2,2),(3,3),(4,1),(5,2),(6,4)"""
        def r = sql "SELECT HLL_UNION_AGG(HLL_HASH(v)), COUNT(DISTINCT v) FROM t_agg_comb_002"
        long hllVal = (long)r[0][0]
        long distVal = (long)r[0][1]
        // HLL approx equal COUNT(DISTINCT) on small data
        assertEquals(distVal, hllVal,
            "HLL_UNION_AGG ~ COUNT(DISTINCT) on small data; hll=${hllVal} distinct=${distVal}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_comb_002" } catch (Exception ignore) {}
    }
}
