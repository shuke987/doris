suite("repro_ct_cmp_017") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_017"
    try {
        sql """
            CREATE TABLE t_ct_cmp_017 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_017 VALUES (1, [1,2]), (2, [1,2]), (3, [3])"
        def r = sql "SELECT DISTINCT arr FROM t_ct_cmp_017 ORDER BY arr"
        assertEquals(2, r.size(), "CT-CMP-017: DISTINCT array; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_017" } catch (Exception ignore) {}
    }
}
