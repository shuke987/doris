// JT-CMP-005: DISTINCT JSONB
suite("repro_jt_cmp_005") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_005"
    try {
        sql """
            CREATE TABLE t_jt_cmp_005 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_005 VALUES (1,'1'),(2,'1'),(3,'2')"
        boolean threw = false
        def r = null
        try { r = sql "SELECT DISTINCT j FROM t_jt_cmp_005" }
        catch (Exception e) { threw = true }
        // Either succeeds with 2 unique values or rejected as cmp predicate
        if (!threw) {
            assertEquals(2, r.size(), "JT-CMP-005; observed=${r}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_005" } catch (Exception ignore) {}
    }
}
