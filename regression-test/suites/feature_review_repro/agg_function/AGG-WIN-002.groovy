// AGG-WIN-002: AVG OVER + PARTITION BY
suite("repro_agg_win_002") {
    sql "DROP TABLE IF EXISTS t_agg_win_002"
    try {
        sql """CREATE TABLE t_agg_win_002 (id INT, k INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_win_002 VALUES
            (1,1,10),(2,1,20),(3,1,30),(4,2,100),(5,2,200)"""
        def r = sql """SELECT id, k, v,
            AVG(v) OVER (PARTITION BY k) AS partavg
            FROM t_agg_win_002 ORDER BY id"""
        // k=1 avg = 20, k=2 avg = 150
        assertEquals(20.0, (double)r[0][3], 1e-9, "partition k=1 avg = 20")
        assertEquals(20.0, (double)r[1][3], 1e-9, "all k=1 rows have same partition avg")
        assertEquals(20.0, (double)r[2][3], 1e-9, "all k=1 rows have same partition avg")
        assertEquals(150.0, (double)r[3][3], 1e-9, "partition k=2 avg = 150")
        assertEquals(150.0, (double)r[4][3], 1e-9, "all k=2 rows have same partition avg")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_win_002" } catch (Exception ignore) {}
    }
}
