suite("repro_ct_cmp_003") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_003"
    try {
        sql """
            CREATE TABLE t_ct_cmp_003 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_003 VALUES (1, [1,NULL]), (2, [1,2])"
        def r = sql "SELECT id FROM t_ct_cmp_003 ORDER BY arr"
        assertEquals(2, r.size(), "CT-CMP-003: NULL element ordering; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_003" } catch (Exception ignore) {}
    }
}
