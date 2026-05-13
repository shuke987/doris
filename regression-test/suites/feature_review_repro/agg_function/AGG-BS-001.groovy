// AGG-BS-001: SUM 整数 hand-computed
// Oracle: SUM(1,2,3,...,10) = 55 (Gauss sum)
suite("repro_agg_bs_001") {
    sql "DROP TABLE IF EXISTS t_agg_bs_001"
    try {
        sql """
            CREATE TABLE t_agg_bs_001 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_bs_001 VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10)"
        def r = sql "SELECT SUM(v), COUNT(v), AVG(v), MIN(v), MAX(v) FROM t_agg_bs_001"
        assertEquals(55L, r[0][0], "SUM(1..10) = 55")
        assertEquals(10L, r[0][1], "COUNT = 10")
        assertEquals(5.5, (double)r[0][2], 0.01, "AVG = 5.5")
        assertEquals(1, r[0][3], "MIN = 1")
        assertEquals(10, r[0][4], "MAX = 10")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bs_001" } catch (Exception ignore) {}
    }
}
