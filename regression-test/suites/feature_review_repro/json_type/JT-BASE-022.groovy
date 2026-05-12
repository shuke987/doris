// JT-BASE-022: RANDOM 分布 + JSONB value 列 应成功
suite("repro_jt_base_022") {
    sql "DROP TABLE IF EXISTS t_jt_base_022"
    try {
        sql """
            CREATE TABLE t_jt_base_022 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY RANDOM BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_022 VALUES (1, '{\"a\":1}')"
        def r = sql "SELECT count(*) FROM t_jt_base_022"
        assertEquals("1", r[0][0].toString(),
            "JT-BASE-022: row count; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_022" } catch (Exception ignore) {}
    }
}
