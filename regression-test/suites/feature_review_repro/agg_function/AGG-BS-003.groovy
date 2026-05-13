// AGG-BS-003: 空 group agg
// Oracle: spec
//   SUM/AVG/MIN/MAX(empty) = NULL
//   COUNT(empty) = 0
suite("repro_agg_bs_003") {
    sql "DROP TABLE IF EXISTS t_agg_bs_003"
    try {
        sql """
            CREATE TABLE t_agg_bs_003 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_bs_003 VALUES (1, 10)"
        def r = sql "SELECT SUM(v), COUNT(v), AVG(v), MIN(v), MAX(v), COUNT(*) FROM t_agg_bs_003 WHERE 1=0"
        assertEquals(null, r[0][0], "SUM(empty) = NULL")
        assertEquals(0L, r[0][1], "COUNT(empty) = 0")
        assertEquals(null, r[0][2], "AVG(empty) = NULL")
        assertEquals(null, r[0][3], "MIN(empty) = NULL")
        assertEquals(null, r[0][4], "MAX(empty) = NULL")
        assertEquals(0L, r[0][5], "COUNT(*) on empty = 0")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bs_003" } catch (Exception ignore) {}
    }
}
