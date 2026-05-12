suite("repro_ct_explode_006") {
    sql "DROP TABLE IF EXISTS t_ct_explode_006"
    try {
        sql """
            CREATE TABLE t_ct_explode_006 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_006 VALUES (1, [1,NULL,3])"
        def r = sql "SELECT x FROM t_ct_explode_006 LATERAL VIEW explode(arr) tmp AS x"
        assertEquals(3, r.size(), "CT-EXPLODE-006: NULL element preserved; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_006" } catch (Exception ignore) {}
    }
}
