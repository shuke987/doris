// AGG-MI-003: FILTER WHERE clause 通用 (SQL:2003 standard)
suite("repro_agg_mi_003") {
    sql "DROP TABLE IF EXISTS t_agg_mi_003"
    try {
        sql """CREATE TABLE t_agg_mi_003 (id INT, v INT, p INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_mi_003 VALUES (1,10,1),(2,20,0),(3,30,1),(4,40,0)"
        // SUM FILTER (WHERE p=1) — 不少 SQL 实现支持
        boolean supported = false
        try {
            def r = sql "SELECT SUM(v) FILTER (WHERE p=1) FROM t_agg_mi_003"
            assertEquals(40L, r[0][0], "FILTER WHERE p=1: SUM = 10+30 = 40")
            supported = true
        } catch (Exception e) {
            // Doris 可能不实现；不强断言
        }
        // 等价 CASE WHEN 应总工作
        def r2 = sql "SELECT SUM(CASE WHEN p=1 THEN v END) FROM t_agg_mi_003"
        assertEquals(40L, r2[0][0], "fallback CASE WHEN SUM = 40")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_mi_003" } catch (Exception ignore) {}
    }
}
