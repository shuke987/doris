// CK-SEV1-003 (SEV-1 DORIS-25643 variant): 单列 unique key extension
// Spec correct: UNIQUE KEY (a) + ORDER BY (a, b) 是合法 cluster key 扩展
// 当前 4.1: 同 #001 sameKey bug → FAIL = bug signal
suite("repro_ck_sev1_003") {
    sql "DROP TABLE IF EXISTS t_ck_sev1_003"
    try {
        sql """
            CREATE TABLE t_ck_sev1_003 (a BIGINT, b BIGINT, payload STRING)
            UNIQUE KEY (a)
            ORDER BY (a, b)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_sev1_003"
        assertTrue(r[0][1].toString().toLowerCase().contains("order by"),
            "Single-col unique key extension MUST be accepted (DORIS-25643). UNIQUE(a) + ORDER BY(a,b) is valid")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_sev1_003" } catch (Exception ignore) {}
    }
}
