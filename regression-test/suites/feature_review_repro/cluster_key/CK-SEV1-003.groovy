// CK-SEV1-003 (SEV-1 #1 variant): 单列 unique key extension 也被拒
suite("repro_ck_sev1_003") {
    sql "DROP TABLE IF EXISTS t_ck_sev1_003"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_sev1_003 (a BIGINT, b BIGINT, payload STRING)
            UNIQUE KEY (a)
            ORDER BY (a, b)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    // SEV-1 #1: UNIQUE(a) + ORDER BY(a, b) 也被拒（cluster key 是 unique key 的扩展）
    assertTrue(threw, "SEV-1 #1: single-col unique key extension also rejected (BUG)")
    sql "DROP TABLE IF EXISTS t_ck_sev1_003"
}
