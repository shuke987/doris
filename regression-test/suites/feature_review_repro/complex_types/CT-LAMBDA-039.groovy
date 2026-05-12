suite("repro_ct_lambda_039") {
    sql "DROP TABLE IF EXISTS t_ct_lambda_039"
    try {
        sql """
            CREATE TABLE t_ct_lambda_039 (id INT, col INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_lambda_039 VALUES (1, 10, [1,2,3])"
        def r = sql "SELECT array_map(x->x+col, arr) FROM t_ct_lambda_039"
        assertTrue(r[0][0] != null, "CT-LAMBDA-039: closure col; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_lambda_039" } catch (Exception ignore) {}
    }
}
