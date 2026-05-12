// JT-CROSS-032: JSONB × GROUP BY 派生列
suite("repro_jt_cross_032") {
    sql "DROP TABLE IF EXISTS t_jt_cross_032"
    try {
        sql """
            CREATE TABLE t_jt_cross_032 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_032 VALUES (1,'{\"k\":\"x\"}'),(2,'{\"k\":\"x\"}'),(3,'{\"k\":\"y\"}')"
        def r = sql "SELECT jsonb_extract_string(j, '\$.k') AS k, count(*) FROM t_jt_cross_032 GROUP BY k ORDER BY k"
        assertEquals(2, r.size(), "JT-CROSS-032; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_032" } catch (Exception ignore) {}
    }
}
