suite("repro_ct_lambda_012") {
    sql "DROP TABLE IF EXISTS t_ct_lambda_012"
    try {
        sql """
            CREATE TABLE t_ct_lambda_012 (id INT, col INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_lambda_012 VALUES (1, 10, [1,2,3])"
        def r = sql "SELECT array_map(x->x+col, arr) FROM t_ct_lambda_012"
        String s = r[0][0].toString()
        assertTrue(s.contains("11") && s.contains("12") && s.contains("13"), "CT-LAMBDA-012: lambda closure outer col; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_lambda_012" } catch (Exception ignore) {}
    }
}
