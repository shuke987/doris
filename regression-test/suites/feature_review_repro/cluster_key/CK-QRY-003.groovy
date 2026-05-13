// CK-QRY-003: cluster key 列范围查询
suite("repro_ck_qry_003") {
    sql "DROP TABLE IF EXISTS t_ck_qry_003"
    try {
        sql """
            CREATE TABLE t_ck_qry_003 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_qry_003 VALUES (1,10,'a'),(2,20,'b'),(3,30,'c'),(4,40,'d'),(5,50,'e')"
        def r1 = sql "SELECT count(*) FROM t_ck_qry_003 WHERE sort_v >= 20 AND sort_v <= 40"
        assertEquals(3L, r1[0][0], "range [20,40] should return 3 rows")
        def r2 = sql "SELECT count(*) FROM t_ck_qry_003 WHERE sort_v < 25"
        assertEquals(2L, r2[0][0], "range < 25 should return 2 rows")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_qry_003" } catch (Exception ignore) {}
    }
}
