// CK-VAL-009: cluster key 含 FLOAT 列 → Nereids 拒绝
suite("repro_ck_val_009") {
    sql "DROP TABLE IF EXISTS t_ck_val_009"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_009 (id BIGINT, score FLOAT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, score)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with FLOAT column should be rejected (Nereids path)")
    sql "DROP TABLE IF EXISTS t_ck_val_009"
}
