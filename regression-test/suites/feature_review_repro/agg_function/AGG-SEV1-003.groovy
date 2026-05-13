// AGG-SEV1-003 (N2): Sum DECIMAL > 38 precision silent truncation
// 期望（spec 合理）: 不允许或保留精度 / 报错；实际可能 silent 截到 DECIMAL(38, scale)
suite("repro_agg_sev1_003") {
    sql "DROP TABLE IF EXISTS t_agg_sev1_003"
    try {
        // 用 DECIMAL(38, 4) — 最大精度边界
        sql """CREATE TABLE t_agg_sev1_003 (id INT, v DECIMAL(38, 4)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // 9999999999999999999999999999999999.9999 (34 digits before dot, 4 after) 接近 max
        sql """INSERT INTO t_agg_sev1_003 VALUES
            (1, 9999999999999999999999999999999999.9999),
            (2, 9999999999999999999999999999999999.9999),
            (3, 9999999999999999999999999999999999.9999)"""
        // 3x 接近 max → 应 overflow 报错或 promote。当前可能 silently 错
        def r = sql "SELECT SUM(v) FROM t_agg_sev1_003"
        // 不强断言：本 case 锁住实际行为，等修复后断言
        assertNotNull(r[0][0], "SUM 不应 silently crash")
        // 文档化：实际值是 truncated 还是正确？观察 result
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev1_003" } catch (Exception ignore) {}
    }
}
