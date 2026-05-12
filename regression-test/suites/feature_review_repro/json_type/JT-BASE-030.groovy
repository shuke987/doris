// JT-BASE-030: ALTER ADD COLUMN JSONB
suite("repro_jt_base_030") {
    sql "DROP TABLE IF EXISTS t_jt_base_030"
    try {
        sql """
            CREATE TABLE t_jt_base_030 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_jt_base_030 ADD COLUMN j JSONB"
        // wait for schema-change to settle: a quick INSERT then check
        sql "INSERT INTO t_jt_base_030 VALUES (1, '{\"a\":1}')"
        def r = sql "SELECT j FROM t_jt_base_030 WHERE id=1"
        assertEquals(1, r.size(), "JT-BASE-030: row missing")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_030" } catch (Exception ignore) {}
    }
}
