// CT-ARRAY-027: ARRAY 列 + RANDOM 分布
suite("repro_ct_array_027") {
    sql "DROP TABLE IF EXISTS t_ct_array_027"
    try {
        sql """
            CREATE TABLE t_ct_array_027 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id)
            DISTRIBUTED BY RANDOM BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_027 VALUES (1, [1,2,3])"
        def r = sql "SELECT a FROM t_ct_array_027"
        assertEquals(1, r.size(), "CT-ARRAY-027: ARRAY column with RANDOM distribution works; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_027" } catch (Exception ignore) {}
    }
}
