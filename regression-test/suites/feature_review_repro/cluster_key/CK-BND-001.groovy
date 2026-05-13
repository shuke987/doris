// CK-BND-001: cluster key 列含 NULL 值
suite("repro_ck_bnd_001") {
    sql "DROP TABLE IF EXISTS t_ck_bnd_001"
    try {
        sql """
            CREATE TABLE t_ck_bnd_001 (id BIGINT, sort_v BIGINT NULL, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_bnd_001 VALUES (1, NULL, 'a'),(2, 10, 'b'),(3, NULL, 'c')"
        def r = sql "SELECT count(*) FROM t_ck_bnd_001"
        assertEquals(3L, r[0][0], "NULL in cluster key column should be allowed")
        // NULLS FIRST: NULL 排在前面
        def r2 = sql "SELECT id FROM t_ck_bnd_001 ORDER BY sort_v"
        // 前两个应是 NULL (id=1, id=3) 顺序未定
        assertNotNull(r2, "ORDER BY on cluster key with NULL should not crash")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_bnd_001" } catch (Exception ignore) {}
    }
}
