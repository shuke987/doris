suite("repro_ct_cmp_004") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_004"
    try {
        sql """
            CREATE TABLE t_ct_cmp_004 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_004 SELECT 1, map('a',1)"
        sql "INSERT INTO t_ct_cmp_004 SELECT 2, map('a',2)"
        def r = sql "SELECT id FROM t_ct_cmp_004 ORDER BY m"
        assertEquals(2, r.size(), "CT-CMP-004: ORDER BY map; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_004" } catch (Exception ignore) {}
    }
}
