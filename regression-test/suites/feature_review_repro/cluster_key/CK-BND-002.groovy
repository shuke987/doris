// CK-BND-002: 大量行 INSERT + cluster key sort 正确性
suite("repro_ck_bnd_002") {
    sql "DROP TABLE IF EXISTS t_ck_bnd_002"
    try {
        sql """
            CREATE TABLE t_ck_bnd_002 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        // 100 行
        StringBuilder vals = new StringBuilder()
        for (int i = 1; i <= 100; i++) {
            if (vals.length() > 0) vals.append(",")
            vals.append("(${i}, ${101 - i}, 'p${i}')")
        }
        sql "INSERT INTO t_ck_bnd_002 VALUES ${vals.toString()}"
        def r = sql "SELECT count(*) FROM t_ck_bnd_002"
        assertEquals(100L, r[0][0], "100 rows should be inserted")
        // sort_v 升序：sort_v=1 (id=100), sort_v=100 (id=1)
        def r2 = sql "SELECT id, sort_v FROM t_ck_bnd_002 ORDER BY sort_v LIMIT 3"
        assertEquals(100L, r2[0][0], "smallest sort_v=1 → id=100")
        assertEquals(99L, r2[1][0], "next sort_v=2 → id=99")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_bnd_002" } catch (Exception ignore) {}
    }
}
