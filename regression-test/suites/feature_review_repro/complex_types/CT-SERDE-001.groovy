suite("repro_ct_serde_001") {
    sql "DROP TABLE IF EXISTS t_ct_serde_001"
    try {
        sql """
            CREATE TABLE t_ct_serde_001 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_serde_001 VALUES (1, [10,20,30])"
        def r = sql "SELECT arr FROM t_ct_serde_001 WHERE id=1"
        String s = r[0][0].toString()
        assertTrue(s.contains("10") && s.contains("30"), "CT-SERDE-001: ARRAY round-trip; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_serde_001" } catch (Exception ignore) {}
    }
}
