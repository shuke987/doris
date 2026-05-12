suite("repro_ct_cmp_011") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_011"
    try {
        sql """
            CREATE TABLE t_ct_cmp_011 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_011 VALUES (1, [1,2]), (2, [1,2]), (3, [3,4])"
        def r = sql "SELECT arr, count(*) FROM t_ct_cmp_011 GROUP BY arr ORDER BY count(*) DESC"
        assertEquals(2, r.size(), "CT-CMP-011: GROUP BY array 2 groups; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_011" } catch (Exception ignore) {}
    }
}
