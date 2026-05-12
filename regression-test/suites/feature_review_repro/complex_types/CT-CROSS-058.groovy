suite("repro_ct_cross_058") {
    sql "DROP TABLE IF EXISTS t_ct_cross_058"
    try {
        sql """
            CREATE TABLE t_ct_cross_058 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_058 VALUES (1,10),(1,20),(2,30)"
        def r = sql "SELECT id, array_size(array_agg(v)) FROM t_ct_cross_058 GROUP BY id ORDER BY id"
        assertEquals(2, r.size(), "CT-CROSS-058: array_agg GROUP BY; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_058" } catch (Exception ignore) {}
    }
}
