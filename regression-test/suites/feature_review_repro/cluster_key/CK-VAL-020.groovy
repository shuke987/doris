// CK-VAL-020: cluster key 含 DECIMAL 列 → 允许
suite("repro_ck_val_020") {
    sql "DROP TABLE IF EXISTS t_ck_val_020"
    try {
        sql """
            CREATE TABLE t_ck_val_020 (id BIGINT, val DECIMAL(10,2), payload STRING)
            UNIQUE KEY (id)
            ORDER BY (val)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_val_020"
        assertTrue(r[0][1].toString().toLowerCase().contains("order by"),
                   "DECIMAL cluster key should be allowed")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_val_020" } catch (Exception ignore) {}
    }
}
