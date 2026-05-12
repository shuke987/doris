suite("repro_ct_lambda_045") {
    sql "DROP TABLE IF EXISTS t_ct_lambda_045"
    try {
        sql """
            CREATE TABLE t_ct_lambda_045 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_lambda_045 VALUES (1, [1,2,3])"
        def r = sql "SELECT id, x FROM t_ct_lambda_045 LATERAL VIEW explode(array_map(y->y*2, arr)) tmp AS x"
        assertEquals(3, r.size(), "CT-LAMBDA-045: lambda + explode; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_lambda_045" } catch (Exception ignore) {}
    }
}
