suite("repro_ct_cmp_029") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_029"
    try {
        sql """
            CREATE TABLE t_ct_cmp_029 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_029 SELECT 1, named_struct('a', 10)"
        sql "INSERT INTO t_ct_cmp_029 SELECT 2, named_struct('a', 3)"
        def r = sql "SELECT count(*) FROM t_ct_cmp_029 WHERE s.a > 5"
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-CMP-029: WHERE struct field; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_029" } catch (Exception ignore) {}
    }
}
