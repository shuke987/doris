// CK-BUILD-006: 多 INSERT 后 unique key + cluster key 数据正确性
suite("repro_ck_build_006") {
    sql "DROP TABLE IF EXISTS t_ck_build_006"
    try {
        sql """
            CREATE TABLE t_ck_build_006 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        // 多次 INSERT 同 id 不同 sort_v
        sql "INSERT INTO t_ck_build_006 VALUES (1, 100, 'a')"
        sql "INSERT INTO t_ck_build_006 VALUES (1, 200, 'b')"
        sql "INSERT INTO t_ck_build_006 VALUES (2, 50, 'c')"
        // unique key (id=1, id=2) → 2 rows
        def r = sql "SELECT count(*) FROM t_ck_build_006"
        assertEquals(2L, r[0][0], "MOW should dedupe by unique key id")
        // id=1 最新是 (1, 200, 'b')
        def r2 = sql "SELECT sort_v, payload FROM t_ck_build_006 WHERE id=1"
        assertEquals(200L, r2[0][0], "id=1 latest sort_v should be 200")
        assertEquals('b', r2[0][1], "id=1 latest payload should be 'b'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_006" } catch (Exception ignore) {}
    }
}
