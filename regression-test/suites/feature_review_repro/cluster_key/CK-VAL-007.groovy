// CK-VAL-007: ORDER BY 不存在的列应拒绝
suite("repro_ck_val_007") {
    sql "DROP TABLE IF EXISTS t_ck_val_007"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_007 (id BIGINT, v BIGINT)
            UNIQUE KEY (id)
            ORDER BY (nonexistent_col)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "ORDER BY with nonexistent column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_007"
}
