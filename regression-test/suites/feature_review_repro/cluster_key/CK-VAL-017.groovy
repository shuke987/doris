// CK-VAL-017: cluster key 含 DOUBLE 列 → 拒绝
suite("repro_ck_val_017") {
    sql "DROP TABLE IF EXISTS t_ck_val_017"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_017 (id BIGINT, d DOUBLE, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (d)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with DOUBLE column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_017"
}
