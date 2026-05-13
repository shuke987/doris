// AGG-INV-004: COUNT 三态不变量
// Oracle: COUNT(*) >= COUNT(col) >= COUNT(DISTINCT col)
suite("repro_agg_inv_004") {
    sql "DROP TABLE IF EXISTS t_agg_inv_004"
    try {
        sql """
            CREATE TABLE t_agg_inv_004 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_inv_004 VALUES (1,10),(2,10),(3,20),(4,NULL),(5,NULL),(6,30)"
        def r = sql "SELECT COUNT(*), COUNT(v), COUNT(DISTINCT v) FROM t_agg_inv_004"
        long cStar = (long)r[0][0]
        long cCol = (long)r[0][1]
        long cDist = (long)r[0][2]
        assertTrue(cStar >= cCol, "COUNT(*) >= COUNT(col): ${cStar} >= ${cCol}")
        assertTrue(cCol >= cDist, "COUNT(col) >= COUNT(DISTINCT): ${cCol} >= ${cDist}")
        assertEquals(6L, cStar, "COUNT(*) = 6")
        assertEquals(4L, cCol, "COUNT(col) skip 2 NULL = 4")
        assertEquals(3L, cDist, "DISTINCT {10,20,30} = 3")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_004" } catch (Exception ignore) {}
    }
}
