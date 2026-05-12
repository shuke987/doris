// JT-BASE-026: 写入 100KB 单 JSONB 值（合理边界，非 stress）
suite("repro_jt_base_026") {
    sql "DROP TABLE IF EXISTS t_jt_base_026"
    try {
        sql """
            CREATE TABLE t_jt_base_026 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // build ~100KB string
        String big = "x" * 50000
        sql "INSERT INTO t_jt_base_026 VALUES (1, '\"${big}\"')"
        def r = sql "SELECT json_length(jsonb_extract(j, '\$')) FROM t_jt_base_026"
        // for scalar string json_length is 1
        assertEquals("1", r[0][0].toString(), "JT-BASE-026: 100KB string; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_026" } catch (Exception ignore) {}
    }
}
