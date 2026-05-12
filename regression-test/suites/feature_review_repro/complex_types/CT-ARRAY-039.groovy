// CT-ARRAY-039: 含 NULL 元素数组 [1, NULL, 3]
suite("repro_ct_array_039") {
    sql "DROP TABLE IF EXISTS t_ct_array_039"
    try {
        sql """
            CREATE TABLE t_ct_array_039 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_039 VALUES (1, [1, NULL, 3])"
        def r = sql "SELECT array_size(a) FROM t_ct_array_039 WHERE id=1"
        assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-039: array with NULL element size=3; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_039" } catch (Exception ignore) {}
    }
}
