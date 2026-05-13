// CK-VAL-006: ORDER BY 重复列应拒绝
suite("repro_ck_val_006") {
    sql "DROP TABLE IF EXISTS t_ck_val_006"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_006 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, v, v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "ORDER BY with duplicate column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_006"
}
