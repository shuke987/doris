// CK-VAL-005: ORDER BY 含 NULLS LAST 应拒绝
suite("repro_ck_val_005") {
    sql "DROP TABLE IF EXISTS t_ck_val_005"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_val_005 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id NULLS LAST)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "ORDER BY with NULLS LAST should be rejected (only NULLS FIRST supported)")
    sql "DROP TABLE IF EXISTS t_ck_val_005"
}
