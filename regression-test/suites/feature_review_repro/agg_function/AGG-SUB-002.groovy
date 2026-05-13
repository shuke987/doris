// AGG-SUB-002: agg + IN subquery
suite("repro_agg_sub_002") {
    sql "DROP TABLE IF EXISTS t_agg_sub_002"
    try {
        sql """CREATE TABLE t_agg_sub_002 (id INT, k INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_sub_002 VALUES
            (1,1,10),(2,1,20),(3,2,30),(4,3,40),(5,3,50)"""
        // 找 SUM > 30 的 k
        def r = sql """SELECT SUM(v) FROM t_agg_sub_002 WHERE k IN (
            SELECT k FROM t_agg_sub_002 GROUP BY k HAVING SUM(v) > 30
        )"""
        // k=3 sum=90>30, k=2 sum=30 not>, k=1 sum=30 not>; only k=3
        assertEquals(90L, r[0][0], "SUM where k in (HAVING SUM>30) = 40+50 = 90")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sub_002" } catch (Exception ignore) {}
    }
}
