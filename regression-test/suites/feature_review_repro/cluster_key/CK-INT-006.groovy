// CK-INT-006: cluster key + light schema change (ADD COLUMN)
suite("repro_ck_int_006") {
    sql "DROP TABLE IF EXISTS t_ck_int_006"
    try {
        sql """
            CREATE TABLE t_ck_int_006 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true", "light_schema_change"="true")
        """
        sql "INSERT INTO t_ck_int_006 VALUES (1, 100, 'a')"
        // light schema change: ADD COLUMN
        sql "ALTER TABLE t_ck_int_006 ADD COLUMN new_col INT DEFAULT '0'"
        def r = sql "SELECT count(*) FROM t_ck_int_006"
        assertEquals(1L, r[0][0], "after ADD COLUMN, existing data should be preserved")
        // cluster key 仍存在
        def ddl = sql "SHOW CREATE TABLE t_ck_int_006"
        assertTrue(ddl[0][1].toString().toLowerCase().contains("order by"),
                   "cluster key should remain after light schema change")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_006" } catch (Exception ignore) {}
    }
}
