// JT-CROSS-042: JSONB GROUP BY extract_int
suite("repro_jt_cross_042") {
    sql "DROP TABLE IF EXISTS t_jt_cross_042"
    try {
        sql """
            CREATE TABLE t_jt_cross_042 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_042 VALUES (1,'{\"k\":1}'),(2,'{\"k\":1}'),(3,'{\"k\":2}')"
        def r = sql "SELECT jsonb_extract_int(j, '\$.k') AS k, count(*) FROM t_jt_cross_042 GROUP BY k ORDER BY k"
        assertEquals(2, r.size(), "JT-CROSS-042; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_042" } catch (Exception ignore) {}
    }
}
