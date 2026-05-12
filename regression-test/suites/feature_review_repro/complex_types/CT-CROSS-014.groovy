suite("repro_ct_cross_014") {
    sql "DROP TABLE IF EXISTS t_ct_cross_014"
    try {
        sql """
            CREATE TABLE t_ct_cross_014 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_014 VALUES (1, NULL)"
        def r = sql "SELECT arr FROM t_ct_cross_014 WHERE id=1"
        assertEquals(null, r[0][0], "CT-CROSS-014: NULL container insert; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_014" } catch (Exception ignore) {}
    }
}
