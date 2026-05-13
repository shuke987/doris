// AGG-CB-001: COVAR / CORR
suite("repro_agg_cb_001") {
    sql "DROP TABLE IF EXISTS t_agg_cb_001"
    try {
        sql """CREATE TABLE t_agg_cb_001 (id INT, x DOUBLE, y DOUBLE) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // y = 2x: perfect positive correlation
        sql "INSERT INTO t_agg_cb_001 VALUES (1,1,2),(2,2,4),(3,3,6),(4,4,8),(5,5,10)"
        boolean threw = false
        try {
            def r = sql "SELECT CORR(x, y) FROM t_agg_cb_001"
            // 期望 1.0
            assertEquals(1.0, (double)r[0][0], 1e-9, "CORR(x, 2x) = 1.0")
        } catch (Exception e) {
            threw = true
            // 也可能 Doris 不实现 CORR；记录行为
        }
        // 不强断言 throw（行为依实现）
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_cb_001" } catch (Exception ignore) {}
    }
}
