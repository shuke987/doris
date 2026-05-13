// AGG-DEC-002: DECIMAL overflow handling
suite("repro_agg_dec_002") {
    sql "DROP TABLE IF EXISTS t_agg_dec_002"
    try {
        sql """CREATE TABLE t_agg_dec_002 (id INT, v DECIMAL(10,2)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // DECIMAL(10,2) max ~ 99999999.99, 多行求和测自动扩精度
        sql """INSERT INTO t_agg_dec_002 VALUES
            (1, 99999999.99),(2, 99999999.99),(3, 99999999.99)"""
        def r = sql "SELECT SUM(v) FROM t_agg_dec_002"
        // 3 * 99999999.99 = 299999999.97，需要 DECIMAL(11+,2)
        String sumStr = r[0][0].toString()
        assertTrue(sumStr.startsWith("299999999.97") || sumStr.contains("299999999.97"),
            "DECIMAL SUM should auto-widen precision; got=${sumStr}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dec_002" } catch (Exception ignore) {}
    }
}
