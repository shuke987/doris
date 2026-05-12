// JT-CMP-010: JSONB 列 + WHERE jsonb_extract_string = const
suite("repro_jt_cmp_010") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_010"
    try {
        sql """
            CREATE TABLE t_jt_cmp_010 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_010 VALUES (1,'{\"k\":\"x\"}'),(2,'{\"k\":\"y\"}')"
        def r = sql "SELECT id FROM t_jt_cmp_010 WHERE jsonb_extract_string(j, '\$.k') = 'x'"
        assertEquals(1, r.size(), "JT-CMP-010; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_010" } catch (Exception ignore) {}
    }
}
