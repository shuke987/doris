// AGG-BND-005: 空表 agg 行为
suite("repro_agg_bnd_005") {
    sql "DROP TABLE IF EXISTS t_agg_bnd_005"
    try {
        sql """CREATE TABLE t_agg_bnd_005 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // 空表，无 GROUP BY
        def r = sql "SELECT COUNT(*), COUNT(v), SUM(v), AVG(v), MIN(v), MAX(v) FROM t_agg_bnd_005"
        assertEquals(0L, r[0][0], "empty table COUNT(*) = 0")
        assertEquals(0L, r[0][1], "empty table COUNT(col) = 0")
        assertEquals(null, r[0][2], "empty table SUM = NULL")
        assertEquals(null, r[0][3], "empty table AVG = NULL")
        assertEquals(null, r[0][4], "empty table MIN = NULL")
        assertEquals(null, r[0][5], "empty table MAX = NULL")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bnd_005" } catch (Exception ignore) {}
    }
}
