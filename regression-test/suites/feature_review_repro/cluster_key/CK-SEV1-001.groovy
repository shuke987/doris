// CK-SEV1-001 (SEV-1 #1): cluster key 是 unique key 的扩展应被允许
// 实测：FE 拒绝 "Unique keys and order keys should be different"
// 期望（修复后）：允许，cluster key 是 unique key 的合法扩展
suite("repro_ck_sev1_001") {
    sql "DROP TABLE IF EXISTS t_ck_sev1_001"
    boolean threw = false
    String msg = ""
    try {
        sql """
            CREATE TABLE t_ck_sev1_001 (
                a BIGINT,
                b BIGINT,
                c BIGINT,
                payload STRING
            )
            UNIQUE KEY (a, b)
            ORDER BY (a, b, c)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
        msg = e.getMessage()
    }
    // 当前 buggy 行为：FE 拒绝
    assertTrue(threw, "SEV-1 #1: FE currently rejects cluster key extension of unique key (BUG)")
    assertTrue(msg.toLowerCase().contains("unique") && (msg.toLowerCase().contains("order") || msg.toLowerCase().contains("cluster")),
               "error msg should mention unique + order/cluster; got=${msg}")
    sql "DROP TABLE IF EXISTS t_ck_sev1_001"
}
