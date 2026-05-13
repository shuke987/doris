// CK-INT-007 (SEV-3 #11): light schema change drop 非 cluster key 列
suite("repro_ck_int_007") {
    sql "DROP TABLE IF EXISTS t_ck_int_007"
    try {
        sql """
            CREATE TABLE t_ck_int_007 (id BIGINT, sort_v BIGINT, payload STRING, extra INT)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true", "light_schema_change"="true")
        """
        sql "INSERT INTO t_ck_int_007 VALUES (1, 100, 'a', 999)"
        // drop 非 cluster key 列
        sql "ALTER TABLE t_ck_int_007 DROP COLUMN extra"
        def r = sql "SELECT count(*) FROM t_ck_int_007"
        assertEquals(1L, r[0][0], "after DROP COLUMN non-cluster-key, data preserved")
        // cluster key 仍存在
        def ddl = sql "SHOW CREATE TABLE t_ck_int_007"
        assertTrue(ddl[0][1].toString().toLowerCase().contains("order by"),
                   "cluster key should remain after DROP non-cluster-key COLUMN")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_007" } catch (Exception ignore) {}
    }
}
