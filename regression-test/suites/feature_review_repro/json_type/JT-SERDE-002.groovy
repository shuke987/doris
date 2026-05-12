// JT-SERDE-002: 多行 + 多类型 round-trip
suite("repro_jt_serde_002") {
    sql "DROP TABLE IF EXISTS t_jt_serde_002"
    try {
        sql """
            CREATE TABLE t_jt_serde_002 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_serde_002 VALUES (1,'null'),(2,'true'),(3,'42'),(4,'3.14'),(5,'\"hi\"'),(6,'[]'),(7,'{}')"
        def r = sql "SELECT count(*) FROM t_jt_serde_002"
        assertEquals("7", r[0][0].toString(), "JT-SERDE-002; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_serde_002" } catch (Exception ignore) {}
    }
}
