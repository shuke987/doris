// JT-CMP-006 (HARD RULE): COUNT(DISTINCT JSONB) MUST reject (object types not in DISTINCT)
suite("repro_jt_cmp_006") {
    sql "DROP TABLE IF EXISTS t_jt_cmp_006"
    try {
        sql """
            CREATE TABLE t_jt_cmp_006 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cmp_006 VALUES (1,'1'),(2,'1'),(3,'2')"
        boolean threw = false
        try { sql "SELECT COUNT(DISTINCT j) FROM t_jt_cmp_006" }
        catch (Exception e) { threw = true }
        assertEquals(true, threw, "COUNT(DISTINCT JSONB) MUST be rejected by FE; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cmp_006" } catch (Exception ignore) {}
    }
}
