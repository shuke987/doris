// CT-ARRAY-002: ARRAY of STRING
suite("repro_ct_array_002") {
    sql "DROP TABLE IF EXISTS t_ct_array_002"
    try {
        sql """
            CREATE TABLE t_ct_array_002 (id INT, a ARRAY<STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_002 VALUES (1, ['a','b','c'])"
        def r = sql "SELECT a FROM t_ct_array_002 WHERE id=1"
        assertEquals(1, r.size(), "CT-ARRAY-002: ARRAY<STRING> insert/select round-trip; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_002" } catch (Exception ignore) {}
    }
}
