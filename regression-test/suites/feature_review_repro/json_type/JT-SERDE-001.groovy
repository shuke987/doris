// JT-SERDE-001: 写入 + 读回 round-trip
suite("repro_jt_serde_001") {
    sql "DROP TABLE IF EXISTS t_jt_serde_001"
    try {
        sql """
            CREATE TABLE t_jt_serde_001 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_serde_001 VALUES (1,'{\"a\":1,\"b\":[1,2,3]}')"
        def r = sql "SELECT j FROM t_jt_serde_001 WHERE id=1"
        String v = r[0][0].toString()
        assertTrue(v.contains("\"a\":1") && v.contains("[1,2,3]"),
            "JT-SERDE-001: round-trip; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_serde_001" } catch (Exception ignore) {}
    }
}
