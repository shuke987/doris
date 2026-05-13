// AGG-INV-007: AVG with NULL = SUM/COUNT(non-null) — 跨多列验证
suite("repro_agg_inv_007") {
    sql "DROP TABLE IF EXISTS t_agg_inv_007"
    try {
        sql """CREATE TABLE t_agg_inv_007 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_inv_007 VALUES (1,10),(2,NULL),(3,20),(4,NULL),(5,30)"
        // SUM=60, COUNT(non-null)=3, AVG=20
        def r = sql "SELECT SUM(v), COUNT(v), AVG(v), SUM(v)/COUNT(v) FROM t_agg_inv_007"
        assertEquals(60L, r[0][0], "SUM=60")
        assertEquals(3L, r[0][1], "COUNT(non-null)=3")
        assertEquals(20.0, (double)r[0][2], 1e-9, "AVG=20")
        assertEquals(20.0, (double)r[0][3], 1e-9, "manual SUM/COUNT=20")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_007" } catch (Exception ignore) {}
    }
}
