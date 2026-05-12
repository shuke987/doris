suite("repro_ct_explode_004") {
    sql "DROP TABLE IF EXISTS t_ct_explode_004"
    try {
        sql """
            CREATE TABLE t_ct_explode_004 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_004 VALUES (1, NULL)"
        def r = sql "SELECT id, x FROM t_ct_explode_004 LATERAL VIEW explode_outer(arr) tmp AS x"
        assertEquals(1, r.size(), "CT-EXPLODE-004: outer NULL = 1 row; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_004" } catch (Exception ignore) {}
    }
}
