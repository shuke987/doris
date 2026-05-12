// CT-ARRAY-003: ARRAY of DECIMAL(10,2)
suite("repro_ct_array_003") {
    sql "DROP TABLE IF EXISTS t_ct_array_003"
    try {
        sql """
            CREATE TABLE t_ct_array_003 (id INT, a ARRAY<DECIMAL(10,2)>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_003 VALUES (1, [1.10, 2.20])"
        def r = sql "SELECT a FROM t_ct_array_003 WHERE id=1"
        assertEquals(1, r.size(), "CT-ARRAY-003: ARRAY<DECIMAL> column; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_003" } catch (Exception ignore) {}
    }
}
