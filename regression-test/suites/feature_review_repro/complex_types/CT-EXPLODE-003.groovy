suite("repro_ct_explode_003") {
    sql "DROP TABLE IF EXISTS t_ct_explode_003"
    try {
        sql """
            CREATE TABLE t_ct_explode_003 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_003 VALUES (1, NULL)"
        def r = sql "SELECT x FROM t_ct_explode_003 LATERAL VIEW explode(arr) tmp AS x"
        assertEquals(0, r.size(), "CT-EXPLODE-003: NULL=0 rows; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_003" } catch (Exception ignore) {}
    }
}
