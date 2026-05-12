suite("repro_ct_explode_024") {
    sql "DROP TABLE IF EXISTS t_ct_explode_024"
    try {
        sql """
            CREATE TABLE t_ct_explode_024 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_024 VALUES (1, [1,2,3])"
        def r = sql "SELECT t.id, x FROM t_ct_explode_024 t LATERAL VIEW explode(t.arr) tmp AS x"
        assertEquals(3, r.size(), "CT-EXPLODE-024: with col ref; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_024" } catch (Exception ignore) {}
    }
}
