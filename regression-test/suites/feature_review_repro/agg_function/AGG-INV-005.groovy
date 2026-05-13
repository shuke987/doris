// AGG-INV-005: 数学不变量 - 加法结合律
// Oracle: SUM(c1) + SUM(c2) = SUM(c1 + c2)（无 NULL 的情况）
suite("repro_agg_inv_005") {
    sql "DROP TABLE IF EXISTS t_agg_inv_005"
    try {
        sql """
            CREATE TABLE t_agg_inv_005 (id INT, c1 INT, c2 INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_inv_005 VALUES
            (1,10,1),(2,20,2),(3,30,3),(4,40,4),(5,50,5)"""
        def r = sql "SELECT SUM(c1), SUM(c2), SUM(c1+c2), SUM(c1)+SUM(c2) FROM t_agg_inv_005"
        long s1 = (long)r[0][0]
        long s2 = (long)r[0][1]
        long sBoth = (long)r[0][2]
        long sSum = (long)r[0][3]
        assertEquals(sBoth, sSum,
            "INVARIANT: SUM(c1)+SUM(c2) = SUM(c1+c2); s1+s2=${sSum} sum_both=${sBoth}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_005" } catch (Exception ignore) {}
    }
}
