// CT-ARRAY-020: UNIQUE/AGGREGATE + ARRAY as value col + REPLACE
suite("repro_ct_array_020") {
    sql "DROP TABLE IF EXISTS t_ct_array_020"
    try {
        sql """
            CREATE TABLE t_ct_array_020 (id INT, a ARRAY<INT> REPLACE)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_020 VALUES (1, [1,2]), (1, [3,4])"
        def r = sql "SELECT a FROM t_ct_array_020 WHERE id=1"
        assertEquals(1, r.size(), "CT-ARRAY-020: AGGREGATE+REPLACE+ARRAY should work; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_020" } catch (Exception ignore) {}
    }
}
