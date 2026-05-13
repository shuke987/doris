// CK-BUILD-002: cluster key 与 unique key 完全不同的列
suite("repro_ck_build_002") {
    sql "DROP TABLE IF EXISTS t_ck_build_002"
    try {
        sql """
            CREATE TABLE t_ck_build_002 (user_id BIGINT, event_time DATETIME, event_type INT, payload STRING)
            UNIQUE KEY (user_id)
            ORDER BY (event_time, event_type)
            DISTRIBUTED BY HASH(user_id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_build_002 VALUES (1,'2026-01-01 10:00:00', 100, 'a'),(2,'2026-01-01 11:00:00',200,'b')"
        def r = sql "SELECT count(*) FROM t_ck_build_002"
        assertEquals(2L, r[0][0], "cluster key totally disjoint from unique key should work")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_002" } catch (Exception ignore) {}
    }
}
