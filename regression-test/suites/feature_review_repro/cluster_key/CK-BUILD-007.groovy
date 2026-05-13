// CK-BUILD-007: DELETE + cluster key 数据正确性
suite("repro_ck_build_007") {
    sql "DROP TABLE IF EXISTS t_ck_build_007"
    try {
        sql """
            CREATE TABLE t_ck_build_007 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_build_007 VALUES (1, 10, 'a'),(2, 20, 'b'),(3, 30, 'c')"
        sql "DELETE FROM t_ck_build_007 WHERE id=2"
        def r = sql "SELECT count(*) FROM t_ck_build_007"
        assertEquals(2L, r[0][0], "after DELETE id=2, should have 2 rows")
        // id=2 不应存在
        assertEquals(0L, sql("SELECT count(*) FROM t_ck_build_007 WHERE id=2")[0][0],
                     "id=2 should be deleted")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_007" } catch (Exception ignore) {}
    }
}
