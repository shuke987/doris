suite("repro_ct_cmp_032") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_032a"
    sql "DROP TABLE IF EXISTS t_ct_cmp_032b"
    try {
        sql """
            CREATE TABLE t_ct_cmp_032a (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_ct_cmp_032b (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_032a VALUES (1, [1,2])"
        sql "INSERT INTO t_ct_cmp_032b VALUES (1, [1,2])"
        def r = sql "SELECT count(*) FROM t_ct_cmp_032a a JOIN t_ct_cmp_032b b ON a.arr = b.arr"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CMP-032: JOIN ON array=; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_032a" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_032b" } catch (Exception ignore) {}
    }
}
