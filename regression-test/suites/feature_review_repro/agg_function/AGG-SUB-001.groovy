// AGG-SUB-001: agg in subquery
suite("repro_agg_sub_001") {
    sql "DROP TABLE IF EXISTS t_agg_sub_001"
    try {
        sql """CREATE TABLE t_agg_sub_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_sub_001 VALUES (1,10),(2,20),(3,30)"
        // scalar subquery
        def r1 = sql "SELECT v, v - (SELECT AVG(v) FROM t_agg_sub_001) AS diff FROM t_agg_sub_001 ORDER BY id"
        // avg = 20.0; diff: -10, 0, 10
        assertEquals(-10.0, (double)r1[0][1], 1e-9, "id=1 diff = -10")
        assertEquals(0.0, (double)r1[1][1], 1e-9, "id=2 diff = 0")
        assertEquals(10.0, (double)r1[2][1], 1e-9, "id=3 diff = 10")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sub_001" } catch (Exception ignore) {}
    }
}
