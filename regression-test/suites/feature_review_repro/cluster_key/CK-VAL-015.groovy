// CK-VAL-015: cluster key 含 VARIANT 列 → 拒绝
suite("repro_ck_val_015") {
    sql "DROP TABLE IF EXISTS t_ck_val_015"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_015 (id BIGINT, v VARIANT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with VARIANT column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_015"
}
