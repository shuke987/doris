suite("repro_ct_cmp_020") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_020"
    try {
        sql """
            CREATE TABLE t_ct_cmp_020 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_020 VALUES (1, [1,2,3]), (2, [4,5])"
        def r = sql "SELECT id FROM t_ct_cmp_020 WHERE arr = array(1,2,3)"
        assertEquals(1, r.size() == 0 ? 0 : (r[0][0] as Number).intValue(), "CT-CMP-020: WHERE arr=lit; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_020" } catch (Exception ignore) {}
    }
}
