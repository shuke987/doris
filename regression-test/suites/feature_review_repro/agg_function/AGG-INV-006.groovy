// AGG-INV-006: GROUP BY + 全局 SUM 不变量
// Oracle: SUM(SUM(v) by k) = SUM(v)（分组求和后再总和 = 全部求和）
suite("repro_agg_inv_006") {
    sql "DROP TABLE IF EXISTS t_agg_inv_006"
    try {
        sql """
            CREATE TABLE t_agg_inv_006 (id INT, k INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_inv_006 VALUES
            (1,1,10),(2,1,20),(3,2,30),(4,2,40),(5,3,50)"""
        def totalRow = sql "SELECT SUM(v) FROM t_agg_inv_006"
        def groupRows = sql "SELECT SUM(v) FROM t_agg_inv_006 GROUP BY k"
        long total = (long)totalRow[0][0]
        long groupSum = 0
        for (def row : groupRows) {
            groupSum += (long)row[0]
        }
        assertEquals(total, groupSum,
            "INVARIANT: SUM(SUM(v) GROUP BY k) = SUM(v) globally; global=${total} group_sum=${groupSum}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_006" } catch (Exception ignore) {}
    }
}
