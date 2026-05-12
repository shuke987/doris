// JT-SERDE-005: DELETE WHERE then SELECT
suite("repro_jt_serde_005") {
    sql "DROP TABLE IF EXISTS t_jt_serde_005"
    try {
        sql """
            CREATE TABLE t_jt_serde_005 (id INT, j JSONB)
            UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1","enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_jt_serde_005 VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}'),(3,'{\"a\":3}')"
        sql "DELETE FROM t_jt_serde_005 WHERE id=2"
        def r = sql "SELECT count(*) FROM t_jt_serde_005"
        assertEquals("2", r[0][0].toString(), "JT-SERDE-005; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_serde_005" } catch (Exception ignore) {}
    }
}
