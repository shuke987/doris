// JT-BASE-055: COALESCE(j, '{}') 对 SQL NULL 行
suite("repro_jt_base_055") {
    sql "DROP TABLE IF EXISTS t_jt_base_055"
    try {
        sql """
            CREATE TABLE t_jt_base_055 (id INT, j JSONB NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_base_055 VALUES (1, NULL)"
        def r = sql "SELECT COALESCE(j, CAST('{}' AS JSONB)) FROM t_jt_base_055 WHERE id=1"
        assertEquals(1, r.size(), "JT-BASE-055: row missing")
        assertEquals("{}", r[0][0].toString(),
            "JT-BASE-055: COALESCE returns '{}' for SQL NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_055" } catch (Exception ignore) {}
    }
}
