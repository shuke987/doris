suite("repro_ct_cmp_001") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_001"
    try {
        sql """
            CREATE TABLE t_ct_cmp_001 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_001 VALUES (1, [3,2,1]), (2, [1,2,3])"
        def r = sql "SELECT id FROM t_ct_cmp_001 ORDER BY arr"
        assertEquals(2, r.size(), "CT-CMP-001: ORDER BY array; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_001" } catch (Exception ignore) {}
    }
}
