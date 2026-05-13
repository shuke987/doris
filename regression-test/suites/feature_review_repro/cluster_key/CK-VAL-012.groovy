// CK-VAL-012: cluster key 含 STRUCT 列 → 拒绝
suite("repro_ck_val_012") {
    sql "DROP TABLE IF EXISTS t_ck_val_012"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_012 (id BIGINT, s STRUCT<a:INT,b:STRING>, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, s)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with STRUCT column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_012"
}
