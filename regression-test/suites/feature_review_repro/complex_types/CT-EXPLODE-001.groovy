suite("repro_ct_explode_001") {
    sql "DROP TABLE IF EXISTS t_ct_explode_001"
    try {
        sql """
            CREATE TABLE t_ct_explode_001 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_001 VALUES (1, [1,2,3])"
        def r = sql "SELECT x FROM t_ct_explode_001 LATERAL VIEW explode(arr) tmp AS x ORDER BY x"
        assertEquals(3, r.size(), "CT-EXPLODE-001: 3 rows; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_001" } catch (Exception ignore) {}
    }
}
