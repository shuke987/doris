// CK-VAL-013: cluster key 含 BITMAP 列 → 拒绝
suite("repro_ck_val_013") {
    sql "DROP TABLE IF EXISTS t_ck_val_013"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_013 (id BIGINT, bm BITMAP BITMAP_UNION, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (bm)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with BITMAP column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_013"
}
