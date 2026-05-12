// CT-ARRAY-004: ARRAY of DATETIME
suite("repro_ct_array_004") {
    sql "DROP TABLE IF EXISTS t_ct_array_004"
    try {
        sql """
            CREATE TABLE t_ct_array_004 (id INT, a ARRAY<DATETIME>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_004 VALUES (1, ['2025-01-01 00:00:00', '2025-12-31 23:59:59'])"
        def r = sql "SELECT array_size(a) FROM t_ct_array_004 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-004: ARRAY<DATETIME> size=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_004" } catch (Exception ignore) {}
    }
}
