suite("repro_ct_map_034") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_034"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_034 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_034 SELECT 1, map('a', CAST(NULL AS INT))"
        def r = sql "SELECT element_at(m, 'a') FROM t_repro_ct_map_034 WHERE id=1"
        assertEquals(null, r[0][0], "CT-MAP-034: NULL value preserved; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_034" } catch (Exception ignore) {}
    }
}
