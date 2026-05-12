// JT-CROSS-031: JSONB × HAVING + jsonb_extract
suite("repro_jt_cross_031") {
    sql "DROP TABLE IF EXISTS t_jt_cross_031"
    try {
        sql """
            CREATE TABLE t_jt_cross_031 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_031 VALUES (1,'{\"a\":1}'),(2,'{\"a\":5}')"
        def r = sql "SELECT id, jsonb_extract_int(j, '\$.a') AS a FROM t_jt_cross_031 HAVING a > 2"
        assertEquals(1, r.size(), "JT-CROSS-031; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_031" } catch (Exception ignore) {}
    }
}
