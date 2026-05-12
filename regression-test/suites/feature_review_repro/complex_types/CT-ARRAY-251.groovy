// CT-ARRAY-251: array_with_constant num_col NULL row returns [] not NULL (NEW-SEV-N14)
suite("repro_ct_array_251") {
    sql "DROP TABLE IF EXISTS t_ct_array_251"
    try {
        sql """
            CREATE TABLE t_ct_array_251 (id INT, n INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_251 VALUES (1, NULL), (2, 3)"
        def r = sql "SELECT id, array_with_constant(n, 'x') FROM t_ct_array_251 ORDER BY id"
        // spec: id=1 (n=NULL) should produce NULL; id=2 should produce ['x','x','x']
        Object nullRow = r[0][1]
        assertEquals(null, nullRow, "CT-ARRAY-251: NULL count row -> NULL (NEW-SEV-N14 currently []); observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_251" } catch (Exception ignore) {}
    }
}
