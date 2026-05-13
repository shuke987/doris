// CK-SEV1-001 (SEV-1 DORIS-25643): cluster key 是 unique key 的扩展应被允许
// Spec correct: UNIQUE KEY (a,b) + ORDER BY (a,b,c) 是 cluster key 扩展 unique key 的合法用例
// 当前 4.1: sameKey 错误判定 → 拒绝 → 本 case FAIL = bug signal
suite("repro_ck_sev1_001") {
    sql "DROP TABLE IF EXISTS t_ck_sev1_001"
    try {
        sql """
            CREATE TABLE t_ck_sev1_001 (
                a BIGINT, b BIGINT, c BIGINT, payload STRING
            )
            UNIQUE KEY (a, b)
            ORDER BY (a, b, c)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        // 期望（修复后）：CREATE TABLE succeeds — cluster key 扩展 unique key 是合法
        def r = sql "SHOW CREATE TABLE t_ck_sev1_001"
        assertTrue(r[0][1].toString().toLowerCase().contains("order by"),
            "cluster key as extension of unique key MUST be accepted (DORIS-25643). UNIQUE(a,b) + ORDER BY(a,b,c) is a documented common usage")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_sev1_001" } catch (Exception ignore) {}
    }
}
