// AGG-BS-002: NULL 处理矩阵
// Oracle: spec doc
//   SUM/AVG/MIN/MAX(全 NULL) = NULL
//   COUNT(*) 计 NULL 行；COUNT(col) 不计 NULL
suite("repro_agg_bs_002") {
    sql "DROP TABLE IF EXISTS t_agg_bs_002"
    try {
        sql """
            CREATE TABLE t_agg_bs_002 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_bs_002 VALUES (1, NULL), (2, NULL), (3, NULL)"
        def r = sql "SELECT SUM(v), COUNT(v), AVG(v), MIN(v), MAX(v), COUNT(*) FROM t_agg_bs_002"
        assertEquals(null, r[0][0], "SUM(all NULL) = NULL")
        assertEquals(0L, r[0][1], "COUNT(col) skip NULL = 0")
        assertEquals(null, r[0][2], "AVG(all NULL) = NULL")
        assertEquals(null, r[0][3], "MIN(all NULL) = NULL")
        assertEquals(null, r[0][4], "MAX(all NULL) = NULL")
        assertEquals(3L, r[0][5], "COUNT(*) counts NULL rows = 3")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bs_002" } catch (Exception ignore) {}
    }
}
