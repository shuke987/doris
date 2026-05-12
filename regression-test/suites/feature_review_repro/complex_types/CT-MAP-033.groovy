suite("repro_ct_map_033") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_033"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_033 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_033 SELECT 1, map('a',1, 'a',2)"
        def r = sql "SELECT map_size(m), element_at(m, 'a') FROM t_repro_ct_map_033 WHERE id=1"
        long sz = (r[0][0] as Number).longValue()
        long v = (r[0][1] as Number).longValue()
        // SEV-3 #N10 last-wins
        assertEquals(1L, sz, "CT-MAP-033: dup key dedup size=1; observed=${r}")
        assertEquals(2L, v, "CT-MAP-033: last-wins value=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_033" } catch (Exception ignore) {}
    }
}
