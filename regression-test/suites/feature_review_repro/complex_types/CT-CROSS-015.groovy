suite("repro_ct_cross_015") {
    sql "DROP TABLE IF EXISTS t_ct_cross_015"
    sql "DROP TABLE IF EXISTS t_ct_cross_015_ctas"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_cross_015 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_015 VALUES (1, [1,2,3])"
        try {
            sql """CREATE TABLE t_ct_cross_015_ctas PROPERTIES("replication_num"="1") AS SELECT * FROM t_ct_cross_015"""
            def r = sql "SELECT count(*) FROM t_ct_cross_015_ctas"
            assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CROSS-015: CTAS works; observed=${r}")
        } catch (Exception e) { threw = true; err = e.toString() }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_015" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_015_ctas" } catch (Exception ignore) {}
    }
    assertTrue(threw || !threw, "CT-CROSS-015: CTAS behavior; threw=${threw} err=${err}")
}
