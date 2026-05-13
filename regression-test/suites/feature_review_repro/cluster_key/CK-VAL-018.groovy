// CK-VAL-018: cluster key 含 DATE / DATETIME 列 → 允许
suite("repro_ck_val_018") {
    sql "DROP TABLE IF EXISTS t_ck_val_018"
    try {
        sql """
            CREATE TABLE t_ck_val_018 (id BIGINT, dt DATETIME, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (dt)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_val_018"
        assertTrue(r[0][1].toString().toLowerCase().contains("order by"),
                   "DATETIME cluster key should be allowed and persisted")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_val_018" } catch (Exception ignore) {}
    }
}
