// AGG-INV-001: AVG = SUM / COUNT (non-null) 数学不变量
// Oracle: 数学定义
suite("repro_agg_inv_001") {
    sql "DROP TABLE IF EXISTS t_agg_inv_001"
    try {
        sql """
            CREATE TABLE t_agg_inv_001 (id INT, v DOUBLE)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """INSERT INTO t_agg_inv_001 VALUES
            (1, 1.5),(2, 2.5),(3, 3.5),(4, 4.5),(5, 5.5),
            (6, NULL),(7, 6.5),(8, 7.5),(9, 8.5),(10, 9.5)"""
        def r = sql "SELECT AVG(v), SUM(v)/COUNT(v), SUM(v), COUNT(v) FROM t_agg_inv_001"
        def avgVal = (double)r[0][0]
        def manualAvg = (double)r[0][1]
        assertEquals(avgVal, manualAvg, 1e-9,
            "INVARIANT: AVG = SUM/COUNT (non-null); actual avg=${avgVal} manual=${manualAvg}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_inv_001" } catch (Exception ignore) {}
    }
}
