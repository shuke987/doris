suite("repro_ct_cross_081") {
    sql "DROP TABLE IF EXISTS t_ct_cross_081"
    try {
        sql """
            CREATE TABLE t_ct_cross_081 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_081 VALUES (1, NULL), (2, array())"
        def r1 = sql "SELECT count(*) FROM t_ct_cross_081 WHERE arr IS NULL"
        def r2 = sql "SELECT count(*) FROM t_ct_cross_081 WHERE arr IS NOT NULL AND array_size(arr) = 0"
        assertEquals(1L, (r1[0][0] as Number).longValue(), "CT-CROSS-081: NULL distinct from empty; observed r1=${r1}")
        assertEquals(1L, (r2[0][0] as Number).longValue(), "CT-CROSS-081: empty distinct from NULL; observed r2=${r2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_081" } catch (Exception ignore) {}
    }
}
