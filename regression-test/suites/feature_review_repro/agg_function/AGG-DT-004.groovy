// AGG-DT-004: COUNT(DISTINCT NULL) = 0
suite("repro_agg_dt_004") {
    sql "DROP TABLE IF EXISTS t_agg_dt_004"
    try {
        sql """
            CREATE TABLE t_agg_dt_004 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_dt_004 VALUES (1,NULL),(2,NULL),(3,NULL)"
        def r = sql "SELECT COUNT(DISTINCT v) FROM t_agg_dt_004"
        assertEquals(0L, r[0][0], "COUNT(DISTINCT NULL only) = 0")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_004" } catch (Exception ignore) {}
    }
}
