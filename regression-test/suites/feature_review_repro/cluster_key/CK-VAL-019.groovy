// CK-VAL-019: cluster key 含 BOOLEAN 列 → 允许
suite("repro_ck_val_019") {
    sql "DROP TABLE IF EXISTS t_ck_val_019"
    try {
        sql """
            CREATE TABLE t_ck_val_019 (id BIGINT, flag BOOLEAN, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (flag)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_val_019"
        assertTrue(r[0][1].toString().toLowerCase().contains("order by"),
                   "BOOLEAN cluster key should be allowed")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_val_019" } catch (Exception ignore) {}
    }
}
