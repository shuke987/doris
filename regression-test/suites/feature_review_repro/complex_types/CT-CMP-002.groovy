suite("repro_ct_cmp_002") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_002"
    try {
        sql """
            CREATE TABLE t_ct_cmp_002 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_002 VALUES (1, [1,2]), (2, [1,2,3])"
        def r = sql "SELECT id FROM t_ct_cmp_002 ORDER BY arr"
        assertEquals(2, r.size(), "CT-CMP-002: size tie-break; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_002" } catch (Exception ignore) {}
    }
}
