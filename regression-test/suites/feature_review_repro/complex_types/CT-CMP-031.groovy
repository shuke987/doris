suite("repro_ct_cmp_031") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_031"
    try {
        sql """
            CREATE TABLE t_ct_cmp_031 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_031 VALUES (1, [1,2,3]), (2, [4,5])"
        def r = sql "SELECT count(*) FROM t_ct_cmp_031 WHERE array_contains(arr, 1)"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CMP-031: WHERE array_contains; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_031" } catch (Exception ignore) {}
    }
}
