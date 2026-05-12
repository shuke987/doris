suite("repro_ct_explode_007") {
    sql "DROP TABLE IF EXISTS t_ct_explode_007"
    try {
        sql """
            CREATE TABLE t_ct_explode_007 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_007 VALUES (1, [1,2]), (2, [3]), (3, [4,5,6])"
        def r = sql "SELECT count(*) FROM t_ct_explode_007 LATERAL VIEW explode(arr) tmp AS x"
        assertEquals(6L, (r[0][0] as Number).longValue(), "CT-EXPLODE-007: 6 total rows; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_007" } catch (Exception ignore) {}
    }
}
