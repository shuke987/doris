// CK-INT-003: cluster key + inverted index 可共存
// 注：cluster key 不允许 string，所以 cluster key 用 BIGINT，inverted index 用 TEXT 列
suite("repro_ck_int_003") {
    sql "DROP TABLE IF EXISTS t_ck_int_003"
    try {
        sql """
            CREATE TABLE t_ck_int_003 (id BIGINT, sort_key BIGINT, content TEXT,
                INDEX content_idx (content) USING INVERTED PROPERTIES('parser'='english'))
            UNIQUE KEY (id)
            ORDER BY (sort_key)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_int_003 VALUES (1, 100, 'hello world'),(2, 200, 'doris fast')"
        def r = sql "SELECT count(*) FROM t_ck_int_003 WHERE content MATCH 'hello'"
        assertEquals(1L, r[0][0], "cluster key + inverted index e2e MATCH should hit")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_003" } catch (Exception ignore) {}
    }
}
