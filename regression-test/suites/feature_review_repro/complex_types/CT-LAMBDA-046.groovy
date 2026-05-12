suite("repro_ct_lambda_046") {
    sql "DROP TABLE IF EXISTS t_ct_lambda_046"
    try {
        sql """
            CREATE TABLE t_ct_lambda_046 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_lambda_046 VALUES (1, [1,2,3]), (2, [-1,-2])"
        def r = sql "SELECT id FROM t_ct_lambda_046 WHERE array_count(x->x>0, arr) > 0 ORDER BY id"
        assertEquals(1, r.size() == 0 ? 0 : (r[0][0] as Number).intValue(), "CT-LAMBDA-046: lambda WHERE; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_lambda_046" } catch (Exception ignore) {}
    }
}
