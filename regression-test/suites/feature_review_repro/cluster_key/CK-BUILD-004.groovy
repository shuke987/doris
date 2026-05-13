// CK-BUILD-004: cluster key 单列
suite("repro_ck_build_004") {
    sql "DROP TABLE IF EXISTS t_ck_build_004"
    try {
        sql """
            CREATE TABLE t_ck_build_004 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_build_004"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains("order by") && ddl.contains("`v`"), "single-column cluster key should work; DDL=${ddl.take(300)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_004" } catch (Exception ignore) {}
    }
}
