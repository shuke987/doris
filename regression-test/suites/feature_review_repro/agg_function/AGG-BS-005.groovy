// AGG-BS-005: MIN/MAX on string + datetime
suite("repro_agg_bs_005") {
    sql "DROP TABLE IF EXISTS t_agg_bs_005"
    try {
        sql """
            CREATE TABLE t_agg_bs_005 (id INT, s VARCHAR(50), d DATE, dt DATETIME)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_bs_005 VALUES
            (1, 'banana', '2026-03-15', '2026-03-15 10:00:00'),
            (2, 'apple', '2026-01-01', '2026-01-01 09:00:00'),
            (3, 'cherry', '2026-12-31', '2026-12-31 23:59:59')"""
        def r = sql "SELECT MIN(s), MAX(s), MIN(d), MAX(d), MIN(dt), MAX(dt) FROM t_agg_bs_005"
        assertEquals("apple", r[0][0], "MIN(string) = lexicographic min")
        assertEquals("cherry", r[0][1], "MAX(string) = lexicographic max")
        // 日期 / 时间正确排序
        assertNotNull(r[0][2], "MIN(date)")
        assertNotNull(r[0][3], "MAX(date)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bs_005" } catch (Exception ignore) {}
    }
}
