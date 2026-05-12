suite("repro_ct_explode_026") {
    sql "DROP TABLE IF EXISTS t_ct_explode_026"
    try {
        sql """
            CREATE TABLE t_ct_explode_026 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_026 VALUES (1, [1,2,2,3])"
        def r = sql "SELECT x, count(*) FROM t_ct_explode_026 LATERAL VIEW explode(arr) tmp AS x GROUP BY x ORDER BY x"
        assertEquals(3, r.size(), "CT-EXPLODE-026: explode+GROUP BY; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_026" } catch (Exception ignore) {}
    }
}
