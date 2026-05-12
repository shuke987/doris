suite("repro_ct_cmp_005") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_005"
    try {
        sql """
            CREATE TABLE t_ct_cmp_005 (id INT, s STRUCT<a:INT,b:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_005 SELECT 1, named_struct('a',2,'b',1)"
        sql "INSERT INTO t_ct_cmp_005 SELECT 2, named_struct('a',1,'b',2)"
        def r = sql "SELECT id FROM t_ct_cmp_005 ORDER BY s"
        assertEquals(2, r.size(), "CT-CMP-005: ORDER BY struct; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_005" } catch (Exception ignore) {}
    }
}
