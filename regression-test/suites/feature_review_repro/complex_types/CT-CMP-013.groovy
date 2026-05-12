suite("repro_ct_cmp_013") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_013"
    try {
        sql """
            CREATE TABLE t_ct_cmp_013 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_013 SELECT 1, named_struct('a',1)"
        sql "INSERT INTO t_ct_cmp_013 SELECT 2, named_struct('a',1)"
        sql "INSERT INTO t_ct_cmp_013 SELECT 3, named_struct('a',2)"
        def r = sql "SELECT s, count(*) FROM t_ct_cmp_013 GROUP BY s ORDER BY count(*) DESC"
        assertEquals(2, r.size(), "CT-CMP-013: GROUP BY struct; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_013" } catch (Exception ignore) {}
    }
}
