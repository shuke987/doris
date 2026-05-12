// JT-MODIFY-060: vec 表上的 json_set
suite("repro_jt_modify_060") {
    sql "DROP TABLE IF EXISTS t_jt_modify_060"
    try {
        sql """
            CREATE TABLE t_jt_modify_060 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_modify_060 VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}')"
        def r = sql "SELECT id, json_set(j, '\$.b', 99) FROM t_jt_modify_060 ORDER BY id"
        assertEquals(2, r.size(), "JT-MODIFY-060; observed=${r}")
        String v1 = r[0][1].toString()
        assertTrue(v1.contains("\"b\":99"), "JT-MODIFY-060 row1; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_modify_060" } catch (Exception ignore) {}
    }
}
