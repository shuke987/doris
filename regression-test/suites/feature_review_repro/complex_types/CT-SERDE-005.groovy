suite("repro_ct_serde_005") {
    sql "DROP TABLE IF EXISTS t_ct_serde_005"
    try {
        sql """
            CREATE TABLE t_ct_serde_005 (id INT, arr ARRAY<MAP<STRING,INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_serde_005 SELECT 1, array(map('a',1), map('b',2))"
        def r = sql "SELECT array_size(arr) FROM t_ct_serde_005 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-SERDE-005: ARRAY<MAP> round-trip; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_serde_005" } catch (Exception ignore) {}
    }
}
