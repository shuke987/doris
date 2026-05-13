// CK-BUILD-008: UPDATE + cluster key 数据正确性
suite("repro_ck_build_008") {
    sql "DROP TABLE IF EXISTS t_ck_build_008"
    try {
        sql """
            CREATE TABLE t_ck_build_008 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_build_008 VALUES (1, 10, 'a'),(2, 20, 'b')"
        sql "UPDATE t_ck_build_008 SET payload='updated' WHERE id=1"
        def r = sql "SELECT payload FROM t_ck_build_008 WHERE id=1"
        assertEquals('updated', r[0][0], "UPDATE should change payload to 'updated'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_008" } catch (Exception ignore) {}
    }
}
