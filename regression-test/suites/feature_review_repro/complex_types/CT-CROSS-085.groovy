suite("repro_ct_cross_085") {
    sql "DROP TABLE IF EXISTS t_ct_cross_085"
    try {
        sql """
            CREATE TABLE t_ct_cross_085 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_085 VALUES (1, [NULL,NULL]), (2, [])"
        def r = sql "SELECT arr, count(*) FROM t_ct_cross_085 GROUP BY arr"
        assertEquals(2, r.size(), "CT-CROSS-085: [NULL,NULL] vs [] hash distinct; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_085" } catch (Exception ignore) {}
    }
}
