// CK-VAL-016: cluster key 含 HLL 列 → 拒绝
suite("repro_ck_val_016") {
    sql "DROP TABLE IF EXISTS t_ck_val_016"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_016 (id BIGINT, h HLL HLL_UNION, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (h)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with HLL column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_016"
}
