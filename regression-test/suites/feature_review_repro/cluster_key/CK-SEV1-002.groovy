// CK-SEV1-002 (SEV-1 #1 variant): cluster key = unique key 完全相同应拒绝（正向校验）
suite("repro_ck_sev1_002") {
    sql "DROP TABLE IF EXISTS t_ck_sev1_002"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_sev1_002 (a BIGINT, b BIGINT, payload STRING)
            UNIQUE KEY (a, b)
            ORDER BY (a, b)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    // 完全相同应被拒（这部分是合理的）
    assertTrue(threw, "cluster key = unique key (identical) should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_sev1_002"
}
