// AGG-BND-001: 单行 group agg
suite("repro_agg_bnd_001") {
    sql "DROP TABLE IF EXISTS t_agg_bnd_001"
    try {
        sql """CREATE TABLE t_agg_bnd_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_bnd_001 VALUES (1, 42)"
        def r = sql "SELECT SUM(v), COUNT(v), AVG(v), MIN(v), MAX(v) FROM t_agg_bnd_001"
        assertEquals(42L, r[0][0], "SUM(single row) = value")
        assertEquals(1L, r[0][1], "COUNT(single row) = 1")
        assertEquals(42.0, (double)r[0][2], 1e-9, "AVG = value")
        assertEquals(42, r[0][3], "MIN = value")
        assertEquals(42, r[0][4], "MAX = value")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bnd_001" } catch (Exception ignore) {}
    }
}
