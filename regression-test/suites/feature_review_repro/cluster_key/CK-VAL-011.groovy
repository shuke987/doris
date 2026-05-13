// CK-VAL-011: cluster key 含 MAP 列 → 拒绝
suite("repro_ck_val_011") {
    sql "DROP TABLE IF EXISTS t_ck_val_011"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_011 (id BIGINT, m MAP<STRING,INT>, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, m)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "cluster key with MAP column should be rejected")
    sql "DROP TABLE IF EXISTS t_ck_val_011"
}
