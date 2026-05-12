suite("repro_ct_cross_082") {
    sql "DROP TABLE IF EXISTS t_ct_cross_082"
    try {
        sql """
            CREATE TABLE t_ct_cross_082 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_082 VALUES (1, NULL), (2, [])"
        def r = sql "SELECT id FROM t_ct_cross_082 WHERE arr IS NULL ORDER BY id"
        assertEquals(1, r.size(), "CT-CROSS-082: only NULL row matched; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_082" } catch (Exception ignore) {}
    }
}
