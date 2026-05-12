suite("repro_ct_cmp_012") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_012"
    try {
        sql """
            CREATE TABLE t_ct_cmp_012 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_012 SELECT 1, map('a',1)"
        sql "INSERT INTO t_ct_cmp_012 SELECT 2, map('a',1)"
        sql "INSERT INTO t_ct_cmp_012 SELECT 3, map('b',2)"
        def r = sql "SELECT m, count(*) FROM t_ct_cmp_012 GROUP BY m ORDER BY count(*) DESC"
        assertEquals(2, r.size(), "CT-CMP-012: GROUP BY map; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_012" } catch (Exception ignore) {}
    }
}
