// AGG-INV-008: MAX-MIN = range; range = MAX - MIN >= 0
suite("repro_agg_inv_008") {
    sql "DROP TABLE IF EXISTS t_agg_inv_008"
    try {
        sql """CREATE TABLE t_agg_inv_008 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_inv_008 VALUES (1,-100),(2,50),(3,200)"
        def r = sql "SELECT MAX(v) - MIN(v) FROM t_agg_inv_008"
        long range = (long)r[0][0]
        assertTrue(range >= 0, "MAX-MIN >= 0; range=${range}")
        assertEquals(300L, range, "range = 200 - (-100) = 300")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_008" } catch (Exception ignore) {}
    }
}
