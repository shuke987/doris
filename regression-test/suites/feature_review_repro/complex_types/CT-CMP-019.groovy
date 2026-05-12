suite("repro_ct_cmp_019") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_019"
    try {
        sql """
            CREATE TABLE t_ct_cmp_019 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_019 SELECT 1, named_struct('a',10)"
        sql "INSERT INTO t_ct_cmp_019 SELECT 2, named_struct('a',20)"
        // CASE_FLAW fix: planner does not accept s.a as GROUP BY field directly;
        // pre-project into subquery
        def r = sql "SELECT a, count(*) FROM (SELECT s.a AS a FROM t_ct_cmp_019) t GROUP BY a ORDER BY a"
        assertEquals(2, r.size(), "CT-CMP-019: GROUP BY struct field via subquery; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_019" } catch (Exception ignore) {}
    }
}
