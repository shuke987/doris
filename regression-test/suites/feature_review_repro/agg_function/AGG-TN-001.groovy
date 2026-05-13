// AGG-TN-001: TOPN_ARRAY 返回最频繁的 N 项
suite("repro_agg_tn_001") {
    sql "DROP TABLE IF EXISTS t_agg_tn_001"
    try {
        sql """CREATE TABLE t_agg_tn_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // 10 出现 3 次，20 出现 2 次，30 出现 1 次
        sql """INSERT INTO t_agg_tn_001 VALUES
            (1,10),(2,10),(3,10),(4,20),(5,20),(6,30)"""
        def r = sql "SELECT TOPN_ARRAY(v, 2) FROM t_agg_tn_001"
        String result = r[0][0].toString()
        // top 2 应含 10（最频繁）
        assertTrue(result.contains("10"),
            "TOPN_ARRAY(v,2) should contain 10 (most frequent); got=${result}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_tn_001" } catch (Exception ignore) {}
    }
}
