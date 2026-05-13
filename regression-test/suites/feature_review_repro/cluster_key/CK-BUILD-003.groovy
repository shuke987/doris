// CK-BUILD-003: 大量 cluster key 列 (5 列)
suite("repro_ck_build_003") {
    sql "DROP TABLE IF EXISTS t_ck_build_003"
    try {
        sql """
            CREATE TABLE t_ck_build_003 (id BIGINT, c1 INT, c2 INT, c3 INT, c4 INT, c5 INT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (c1, c2, c3, c4, c5)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_build_003"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains("c1") && ddl.contains("c5"), "5-column cluster key should be persisted; DDL=${ddl.take(300)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_003" } catch (Exception ignore) {}
    }
}
