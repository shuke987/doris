// AGG-LI-001 (R4-2: SUM(LARGEINT/Int128) 也 wrap, 无 promotion 路径可救)
// Spec: SUM 应防溢出。LARGEINT 是最大类型，溢出后无更大类型可 promote。
// 期望: 至少抛 error（strict mode），不应静默 wrap。
// 当前 4.1: 2 × INT128_MAX → -2（静默 wrap）→ 最严重整数类型 SUM 完全不安全
suite("repro_agg_li_001") {
    sql "DROP TABLE IF EXISTS t_agg_li_001"
    try {
        sql """
            CREATE TABLE t_agg_li_001 (id INT, v LARGEINT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_li_001 VALUES (1, 170141183460469231731687303715884105727), (2, 170141183460469231731687303715884105727)"
        boolean threw = false; String obs = "NONE"
        try {
            def r = sql "SELECT SUM(v) FROM t_agg_li_001"
            obs = r[0][0].toString()
        } catch (Exception e) { threw = true }
        // 期望（任一即可）:
        //   1. 抛 overflow error（strict 模式）
        //   2. 返正确值（要求 BE 支持 256-bit 累加器）
        // 不允许: 静默 wrap 成 -2
        assertTrue(threw || obs == "340282366920938463463374607431768211454",
            "SUM(LARGEINT) overflow must error or compute correctly; got wrap=${obs}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_li_001" } catch (Exception ignore) {}
    }
}
