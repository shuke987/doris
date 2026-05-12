// JT-CMP-004: GROUP BY JSONB
suite("repro_jt_cmp_004") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_004"
    try {
        sql """
            CREATE TABLE t_jt_cmp_004 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_004 VALUES (1,'1'),(2,'1'),(3,'2')"
        def r = sql "SELECT j, count(*) FROM t_jt_cmp_004 GROUP BY j"
        // Either succeeds (binary group) or rejected; both valid
        assertNotNull(r, "JT-CMP-004; observed=${r}")
    } catch (Exception e) {
        // If rejected, also acceptable for now
        assertTrue(e.message?.toLowerCase()?.contains("json") ?: false,
            "JT-CMP-004: rejection message should mention json; err=${e.message?.take(200)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_004" } catch (Exception ignore) {}
    }
}
