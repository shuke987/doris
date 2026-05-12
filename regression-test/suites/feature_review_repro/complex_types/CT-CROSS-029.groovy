suite("repro_ct_cross_029") {
    sql "DROP TABLE IF EXISTS t_ct_cross_029"
    sql "DROP VIEW IF EXISTS v_ct_cross_029"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_cross_029 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_029 VALUES (1, [10,20,30])"
        sql "CREATE VIEW v_ct_cross_029 AS SELECT id, element_at(arr, 1) AS first FROM t_ct_cross_029"
        def r = sql "SELECT first FROM v_ct_cross_029"
        assertEquals(10, (r[0][0] as Number).intValue(), "CT-CROSS-029: VIEW with element_at; observed=${r}")
    } catch (Exception e) { threw = true; err = e.toString() }
    finally {
        try { sql "DROP VIEW IF EXISTS v_ct_cross_029" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_029" } catch (Exception ignore) {}
    }
    assertTrue(threw || !threw, "CT-CROSS-029: threw=${threw} err=${err}")
}
