suite("repro_ct_cmp_021") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_021"
    try {
        sql """
            CREATE TABLE t_ct_cmp_021 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_021 VALUES (1, [1,2,3]), (2, [4,5])"
        def r = sql "SELECT count(*) FROM t_ct_cmp_021 WHERE arr <> array(1,2,3)"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CMP-021: WHERE arr<>; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_021" } catch (Exception ignore) {}
    }
}
