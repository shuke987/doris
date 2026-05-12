suite("repro_ct_cross_030") {
    sql "DROP TABLE IF EXISTS t_ct_cross_030"
    sql "DROP VIEW IF EXISTS v_ct_cross_030"
    sql "DROP VIEW IF EXISTS v2_ct_cross_030"
    try {
        sql """
            CREATE TABLE t_ct_cross_030 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_030 VALUES (1, [1,2,3])"
        sql "CREATE VIEW v_ct_cross_030 AS SELECT id, arr FROM t_ct_cross_030"
        sql "CREATE VIEW v2_ct_cross_030 AS SELECT id FROM v_ct_cross_030 WHERE array_contains(arr, 2)"
        def r = sql "SELECT count(*) FROM v2_ct_cross_030"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CROSS-030: nested view array_contains; observed=${r}")
    } finally {
        try { sql "DROP VIEW IF EXISTS v2_ct_cross_030" } catch (Exception ignore) {}
        try { sql "DROP VIEW IF EXISTS v_ct_cross_030" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_030" } catch (Exception ignore) {}
    }
}
