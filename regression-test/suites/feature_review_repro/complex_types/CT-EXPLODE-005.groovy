suite("repro_ct_explode_005") {
    sql "DROP TABLE IF EXISTS t_ct_explode_005"
    try {
        sql """
            CREATE TABLE t_ct_explode_005 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_005 SELECT 1, array()"
        def r = sql "SELECT id, x FROM t_ct_explode_005 LATERAL VIEW explode_outer(arr) tmp AS x"
        // spec: 1 row NULL or 0 rows
        assertTrue(r.size() == 0 || r.size() == 1, "CT-EXPLODE-005: outer empty array; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_005" } catch (Exception ignore) {}
    }
}
