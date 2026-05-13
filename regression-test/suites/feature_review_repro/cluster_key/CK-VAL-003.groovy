// CK-VAL-003: UNIQUE_KEYS + MOW=false (Nereids 路径) → 拒绝
suite("repro_ck_val_003") {
    sql "DROP TABLE IF EXISTS t_ck_val_003"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_003 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="false")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "Nereids path: UNIQUE_KEYS + ORDER BY + MOW=false should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_003"
}
