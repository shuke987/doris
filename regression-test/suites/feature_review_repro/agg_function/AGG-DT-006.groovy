// AGG-DT-006: HLL_UNION_AGG 数学性质
// Oracle: HLL 是近似的，但小数据应等于精确值（< sketch threshold）
suite("repro_agg_dt_006") {
    sql "DROP TABLE IF EXISTS t_agg_dt_006"
    try {
        sql """
            CREATE TABLE t_agg_dt_006 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_dt_006 VALUES
            (1,100),(2,200),(3,100),(4,300),(5,200),(6,400)"""
        def r = sql "SELECT HLL_UNION_AGG(HLL_HASH(v)), COUNT(DISTINCT v) FROM t_agg_dt_006"
        long hllVal = (long)r[0][0]
        long distinctVal = (long)r[0][1]
        assertEquals(distinctVal, hllVal,
            "HLL_UNION_AGG should match COUNT(DISTINCT) on small data; hll=${hllVal} distinct=${distinctVal}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_006" } catch (Exception ignore) {}
    }
}
