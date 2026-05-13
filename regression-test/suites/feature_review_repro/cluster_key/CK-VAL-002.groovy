// CK-VAL-002: DUP_KEYS 表不允许 ORDER BY
suite("repro_ck_val_002") {
    sql "DROP TABLE IF EXISTS t_ck_val_002"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_002 (id BIGINT, v BIGINT)
            DUPLICATE KEY (id)
            ORDER BY (id, v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "DUPLICATE_KEYS table should reject ORDER BY clause")
    sql "DROP TABLE IF EXISTS t_ck_val_002"
}
