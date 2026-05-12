suite("repro_ct_map_025") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_025"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_025 (id INT, m MAP<STRING,INT> DEFAULT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_025 (id) VALUES (1)"
        def r = sql "SELECT m FROM t_repro_ct_map_025 WHERE id=1"
        assertEquals(null, r[0][0], "CT-MAP-025: DEFAULT NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_025" } catch (Exception ignore) {}
    }
}
