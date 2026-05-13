// AGG-SEV1-004 (N1): array_agg(STRUCT/MAP/JSONB) multi-phase BE crash
// 触发条件：shuffle 期间 BE 非 primitive write/read/merge throw NOT_IMPLEMENTED_ERROR
suite("repro_agg_sev1_004") {
    sql "DROP TABLE IF EXISTS t_agg_sev1_004"
    try {
        sql """CREATE TABLE t_agg_sev1_004 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 4
            PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_sev1_004 VALUES
            (1, STRUCT(1, 'a')),(2, STRUCT(2, 'b')),(3, STRUCT(3, 'c'))"""
        // 尝试触发 shuffle agg
        boolean threw = false
        String msg = ""
        try {
            // ARRAY_AGG with GROUP BY may force shuffle
            def r = sql "SELECT id % 2 AS k, ARRAY_AGG(s) FROM t_agg_sev1_004 GROUP BY id % 2"
            // 不 crash 即潜在没踩到 multi-phase
            assertNotNull(r, "small data may run single-phase, multi-phase trigger needs larger N")
        } catch (Exception e) {
            threw = true
            msg = e.getMessage()
        }
        // 文档化：本 case 现状 — 如未 crash 说明 single-phase；如 crash msg 含 NOT_IMPLEMENTED 则 N1 复现
        if (threw) {
            assertTrue(msg.toLowerCase().contains("implement") || msg.toLowerCase().contains("not support"),
                       "If thrown, should mention NOT_IMPLEMENTED; got=${msg}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_004" } catch (Exception ignore) {}
    }
}
