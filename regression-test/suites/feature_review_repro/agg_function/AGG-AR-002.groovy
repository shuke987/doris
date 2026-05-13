// AGG-AR-002 (feature gap): ARRAY_AGG ORDER BY 不支持
// 实测：ARRAY_AGG(v ORDER BY id) → Can not found function 'ARRAY_AGG' which has 2 arity
suite("repro_agg_ar_002") {
    sql "DROP TABLE IF EXISTS t_agg_ar_002"
    try {
        sql """CREATE TABLE t_agg_ar_002 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_ar_002 VALUES (1,10),(2,20)"
        boolean threw = false
        String msg = ""
        try {
            sql "SELECT ARRAY_AGG(v ORDER BY id) FROM t_agg_ar_002"
        } catch (Exception e) {
            threw = true
            msg = e.getMessage()
        }
        assertTrue(threw,
            "FEATURE GAP: ARRAY_AGG(expr ORDER BY ...) not supported (only 1-arity); MySQL/Postgres support this")
        assertTrue(msg.contains("ARRAY_AGG") && msg.toLowerCase().contains("arity"),
            "Error should mention arity; got=${msg}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_ar_002" } catch (Exception ignore) {}
    }
}
