// CK-VAL-008: ORDER BY 空列表应拒绝
suite("repro_ck_val_008") {
    sql "DROP TABLE IF EXISTS t_ck_val_008"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_008 (id BIGINT, v BIGINT)
            UNIQUE KEY (id)
            ORDER BY ()
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "ORDER BY () empty list should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_008"
}
