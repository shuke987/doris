// CK-VAL-001: AGG_KEYS 表不允许 ORDER BY
suite("repro_ck_val_001") {
    sql "DROP TABLE IF EXISTS t_ck_val_001"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_001 (id BIGINT, v BIGINT SUM)
            AGGREGATE KEY (id)
            ORDER BY (id)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "AGG_KEYS table should reject ORDER BY clause")
    sql "DROP TABLE IF EXISTS t_ck_val_001"
}
