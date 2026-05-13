// AGG-STR-001: 字符串 case-sensitive distinct
suite("repro_agg_str_001") {
    sql "DROP TABLE IF EXISTS t_agg_str_001"
    try {
        sql """CREATE TABLE t_agg_str_001 (id INT, s VARCHAR(50)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_str_001 VALUES (1,'hello'),(2,'world'),(3,'HELLO'),(4,'Hello')"
        def r1 = sql "SELECT COUNT(DISTINCT s) FROM t_agg_str_001"
        assertEquals(4L, r1[0][0], "DISTINCT is case-sensitive: 4 unique strings")

        def r2 = sql "SELECT COUNT(DISTINCT UPPER(s)) FROM t_agg_str_001"
        assertEquals(2L, r2[0][0], "DISTINCT UPPER: 'HELLO','WORLD' = 2")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_str_001" } catch (Exception ignore) {}
    }
}
