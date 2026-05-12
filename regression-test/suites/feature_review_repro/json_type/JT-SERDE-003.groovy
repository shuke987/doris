// JT-SERDE-003: 大对象写入 + 读回
suite("repro_jt_serde_003") {
    sql "DROP TABLE IF EXISTS t_jt_serde_003"
    try {
        sql """
            CREATE TABLE t_jt_serde_003 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // 100-key object
        def parts = (1..50).collect { "\"k${it}\":${it}" }.join(",")
        sql "INSERT INTO t_jt_serde_003 VALUES (1, '{${parts}}')"
        def r = sql "SELECT json_length(j) FROM t_jt_serde_003 WHERE id=1"
        assertEquals("50", r[0][0].toString(), "JT-SERDE-003; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_serde_003" } catch (Exception ignore) {}
    }
}
