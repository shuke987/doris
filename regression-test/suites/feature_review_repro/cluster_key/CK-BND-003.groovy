// CK-BND-003: cluster key 列上极值 (BIGINT MIN/MAX)
suite("repro_ck_bnd_003") {
    sql "DROP TABLE IF EXISTS t_ck_bnd_003"
    try {
        sql """
            CREATE TABLE t_ck_bnd_003 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_bnd_003 VALUES (1, -9223372036854775808, 'min'),(2, 9223372036854775807, 'max'),(3, 0, 'zero')"
        def r = sql "SELECT id FROM t_ck_bnd_003 ORDER BY sort_v"
        assertEquals(1L, r[0][0], "MIN should sort first")
        assertEquals(3L, r[1][0], "zero next")
        assertEquals(2L, r[2][0], "MAX last")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_bnd_003" } catch (Exception ignore) {}
    }
}
