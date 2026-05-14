// AGG-TOPN-001 (R5-3: topn(s, N) 在 N<=0 时返 {}, 无参数校验)
// Spec: top-N 中 N 必须 > 0; N <= 0 应报 error 或文档明示行为.
// 当前 4.1: topn(s, -1) 和 topn(s, 0) 都静默返 {} → 调用者无法分辨 "无数据" vs "无效参数".
suite("repro_agg_topn_001") {
    sql "DROP TABLE IF EXISTS t_agg_topn_001"
    try {
        sql """
            CREATE TABLE t_agg_topn_001 (id INT, s VARCHAR(10))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_topn_001 VALUES (1,'a'),(2,'b'),(3,'a'),(4,'b'),(5,'a')"
        boolean threwNeg = false; String obsNeg = "NONE"
        try {
            def r = sql "SELECT topn(s, -1) FROM t_agg_topn_001"
            obsNeg = r[0][0].toString()
        } catch (Exception e) { threwNeg = true }
        // 期望: N<0 抛 error (推荐), 或至少 doc 警告; 不应 silent empty
        assertTrue(threwNeg,
            "topn(s, -1) must error on invalid N (negative); current silently returns ${obsNeg}, indistinguishable from empty data")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_topn_001" } catch (Exception ignore) {}
    }
}
