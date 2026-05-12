suite("repro_ct_serde_004") {
    sql "DROP TABLE IF EXISTS t_ct_serde_004"
    try {
        sql """
            CREATE TABLE t_ct_serde_004 (id INT, arr ARRAY<ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_serde_004 SELECT 1, array(array(1,2), array(3,4))"
        def r = sql "SELECT arr FROM t_ct_serde_004 WHERE id=1"
        assertTrue(r[0][0] != null, "CT-SERDE-004: nested ARRAY; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_serde_004" } catch (Exception ignore) {}
    }
}
