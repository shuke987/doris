// CT-ARRAY-040: 全 NULL 元素 [NULL, NULL]
suite("repro_ct_array_040") {
    sql "DROP TABLE IF EXISTS t_ct_array_040"
    try {
        sql """
            CREATE TABLE t_ct_array_040 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_040 VALUES (1, [NULL, NULL])"
        def r = sql "SELECT array_size(a) FROM t_ct_array_040 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-040: all-NULL array size=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_040" } catch (Exception ignore) {}
    }
}
