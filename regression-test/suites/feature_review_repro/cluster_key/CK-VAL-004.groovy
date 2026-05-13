// CK-VAL-004: ORDER BY 含 DESC 应拒绝
suite("repro_ck_val_004") {
    sql "DROP TABLE IF EXISTS t_ck_val_004"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_004 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id DESC)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "ORDER BY with DESC should be rejected for cluster key (only ASC supported)")
    sql "DROP TABLE IF EXISTS t_ck_val_004"
}
