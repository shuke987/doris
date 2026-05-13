// AGG-DT-005: COLLECT_SET vs COLLECT_LIST
// Oracle:
//   COLLECT_LIST 保留所有非 NULL 项含重复，长度 = COUNT(col)
//   COLLECT_SET 去重，长度 = COUNT(DISTINCT col)
suite("repro_agg_dt_005") {
    sql "DROP TABLE IF EXISTS t_agg_dt_005"
    try {
        sql """
            CREATE TABLE t_agg_dt_005 (id INT, v VARCHAR(20))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_dt_005 VALUES (1,'a'),(2,'b'),(3,'a'),(4,'c'),(5,'b')"
        def r1 = sql "SELECT SIZE(COLLECT_LIST(v)) FROM t_agg_dt_005"
        assertEquals(5L, r1[0][0], "COLLECT_LIST size = COUNT(col) = 5")
        def r2 = sql "SELECT SIZE(COLLECT_SET(v)) FROM t_agg_dt_005"
        assertEquals(3L, r2[0][0], "COLLECT_SET size = COUNT(DISTINCT) = 3")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_005" } catch (Exception ignore) {}
    }
}
