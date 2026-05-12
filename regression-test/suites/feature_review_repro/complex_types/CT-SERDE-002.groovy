suite("repro_ct_serde_002") {
    sql "DROP TABLE IF EXISTS t_ct_serde_002"
    try {
        sql """
            CREATE TABLE t_ct_serde_002 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_serde_002 SELECT 1, map('a',1,'b',2)"
        def r = sql "SELECT element_at(m,'a') FROM t_ct_serde_002 WHERE id=1"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-SERDE-002: MAP round-trip; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_serde_002" } catch (Exception ignore) {}
    }
}
