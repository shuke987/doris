// CT-ARRAY-028: ARRAY 列 DEFAULT NULL
suite("repro_ct_array_028") {
    sql "DROP TABLE IF EXISTS t_ct_array_028"
    try {
        sql """
            CREATE TABLE t_ct_array_028 (id INT, a ARRAY<INT> DEFAULT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_028 (id) VALUES (1)"
        def r = sql "SELECT a FROM t_ct_array_028 WHERE id=1"
        assertEquals(null, r[0][0], "CT-ARRAY-028: ARRAY DEFAULT NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_028" } catch (Exception ignore) {}
    }
}
