// AGG-WIN-001: SUM OVER ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW = running sum
// Oracle: 累积求和
suite("repro_agg_win_001") {
    sql "DROP TABLE IF EXISTS t_agg_win_001"
    try {
        sql """CREATE TABLE t_agg_win_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_win_001 VALUES (1,10),(2,20),(3,30),(4,40),(5,50)"
        def r = sql """SELECT id, v,
            SUM(v) OVER (ORDER BY id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running
            FROM t_agg_win_001 ORDER BY id"""
        // running: 10, 30, 60, 100, 150
        assertEquals(10L, r[0][2], "running sum row 1")
        assertEquals(30L, r[1][2], "running sum row 2")
        assertEquals(60L, r[2][2], "running sum row 3")
        assertEquals(100L, r[3][2], "running sum row 4")
        assertEquals(150L, r[4][2], "running sum row 5")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_win_001" } catch (Exception ignore) {}
    }
}
