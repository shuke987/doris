// AGG-DEC-001: DECIMAL 精度 SUM
suite("repro_agg_dec_001") {
    sql "DROP TABLE IF EXISTS t_agg_dec_001"
    try {
        sql """CREATE TABLE t_agg_dec_001 (id INT, v DECIMAL(20,4)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_dec_001 VALUES (1, 1.0001),(2, 2.0002),(3, 3.0003)"
        def r = sql "SELECT SUM(v), AVG(v) FROM t_agg_dec_001"
        // 精确 SUM = 6.0006, AVG = 2.0002
        assertEquals("6.0006", r[0][0].toString(),
            "DECIMAL SUM precise; got=${r[0][0]}")
        // AVG decimal precision check
        String avg = r[0][1].toString()
        assertTrue(avg.startsWith("2.0002"),
            "DECIMAL AVG should preserve precision; got=${avg}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dec_001" } catch (Exception ignore) {}
    }
}
