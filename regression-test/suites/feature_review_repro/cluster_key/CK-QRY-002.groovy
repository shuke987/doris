// CK-QRY-002: cluster key 列上 ORDER BY 排序
suite("repro_ck_qry_002") {
    sql "DROP TABLE IF EXISTS t_ck_qry_002"
    try {
        sql """
            CREATE TABLE t_ck_qry_002 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_qry_002 VALUES (1, 30, 'a'),(2, 10, 'b'),(3, 20, 'c')"
        def r = sql "SELECT id FROM t_ck_qry_002 ORDER BY sort_v"
        assertEquals(2L, r[0][0], "sort_v ascending: first should be id=2 (sort_v=10)")
        assertEquals(3L, r[1][0], "sort_v ascending: second should be id=3 (sort_v=20)")
        assertEquals(1L, r[2][0], "sort_v ascending: third should be id=1 (sort_v=30)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_qry_002" } catch (Exception ignore) {}
    }
}
