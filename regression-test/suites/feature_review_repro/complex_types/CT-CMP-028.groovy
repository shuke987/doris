suite("repro_ct_cmp_028") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_028"
    try {
        sql """
            CREATE TABLE t_ct_cmp_028 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_028 VALUES (1, NULL)"
        sql "INSERT INTO t_ct_cmp_028 SELECT 2, map('a',1)"
        def r1 = sql "SELECT count(*) FROM t_ct_cmp_028 WHERE m IS NULL"
        def r2 = sql "SELECT count(*) FROM t_ct_cmp_028 WHERE m IS NOT NULL"
        assertEquals(1L, (r1[0][0] as Number).longValue(), "CT-CMP-028a: IS NULL=1; observed=${r1}")
        assertEquals(1L, (r2[0][0] as Number).longValue(), "CT-CMP-028b: IS NOT NULL=1; observed=${r2}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_028" } catch (Exception ignore) {}
    }
}
