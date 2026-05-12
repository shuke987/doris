suite("repro_ct_map_030") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_030"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_030 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_030 SELECT 1, map()"
        def r = sql "SELECT map_size(m) FROM t_repro_ct_map_030 WHERE id=1"
        assertEquals(0L, (r[0][0] as Number).longValue(), "CT-MAP-030: empty map size=0; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_030" } catch (Exception ignore) {}
    }
}
