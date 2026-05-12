suite("repro_ct_cmp_030") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_030"
    try {
        sql """
            CREATE TABLE t_ct_cmp_030 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_030 VALUES (1, [10]), (2, [20])"
        def r = sql "SELECT count(*) FROM t_ct_cmp_030 WHERE arr[1] = 10"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CMP-030: WHERE arr[1]; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_030" } catch (Exception ignore) {}
    }
}
