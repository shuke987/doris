// AGG-BND-003: NULL 谓词的 agg 兼容
suite("repro_agg_bnd_003") {
    sql "DROP TABLE IF EXISTS t_agg_bnd_003"
    try {
        sql """CREATE TABLE t_agg_bnd_003 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_bnd_003 VALUES (1,10),(2,20),(3,30),(4,NULL)"
        // NULL IS NULL filter
        def r1 = sql "SELECT COUNT(*) FROM t_agg_bnd_003 WHERE v IS NULL"
        assertEquals(1L, r1[0][0], "COUNT WHERE v IS NULL = 1")
        // NULL IS NOT NULL
        def r2 = sql "SELECT SUM(v) FROM t_agg_bnd_003 WHERE v IS NOT NULL"
        assertEquals(60L, r2[0][0], "SUM WHERE v IS NOT NULL = 60")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bnd_003" } catch (Exception ignore) {}
    }
}
