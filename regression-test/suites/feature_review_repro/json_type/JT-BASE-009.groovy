// JT-BASE-009: JSONB 列名含中文
suite("repro_jt_base_009") {
    sql "DROP TABLE IF EXISTS t_jt_base_009"
    try {
        sql """
            CREATE TABLE t_jt_base_009 (id INT, `数据` JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_009 VALUES (1, '{\"a\":1}')"
        def r = sql "SELECT count(*) FROM t_jt_base_009"
        assertEquals("1", r[0][0].toString(), "JT-BASE-009; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_009" } catch (Exception ignore) {}
    }
}
