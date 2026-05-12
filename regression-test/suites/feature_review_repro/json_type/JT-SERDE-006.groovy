// JT-SERDE-006: 写入 + ORDER BY + LIMIT 持久化路径
suite("repro_jt_serde_006") {
    sql "DROP TABLE IF EXISTS t_jt_serde_006"
    try {
        sql """
            CREATE TABLE t_jt_serde_006 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_serde_006 VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}'),(3,'{\"a\":3}')"
        def r = sql "SELECT j FROM t_jt_serde_006 ORDER BY id DESC LIMIT 1"
        assertEquals(1, r.size(), "JT-SERDE-006; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_serde_006" } catch (Exception ignore) {}
    }
}
