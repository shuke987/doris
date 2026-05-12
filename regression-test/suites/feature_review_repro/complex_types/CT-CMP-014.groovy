suite("repro_ct_cmp_014") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_014"
    try {
        sql """
            CREATE TABLE t_ct_cmp_014 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_014 VALUES (1, []), (2, []), (3, [1])"
        def r = sql "SELECT arr, count(*) FROM t_ct_cmp_014 GROUP BY arr ORDER BY count(*) DESC"
        assertEquals(2, r.size(), "CT-CMP-014: empty array group; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_014" } catch (Exception ignore) {}
    }
}
