suite("repro_ct_map_097") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_097"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_097 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_097 SELECT 1, map('a',1,'b',2,'c',3)"
        def r = sql "SELECT element_at(m,'a'), element_at(m,'b'), element_at(m,'c') FROM t_repro_ct_map_097"
        // SEV-2 #N4: multiple element_at on same col should work
        assertEquals(1L, (r[0][0] as Number).longValue(), "CT-MAP-097a; observed=${r}")
        assertEquals(2L, (r[0][1] as Number).longValue(), "CT-MAP-097b; observed=${r}")
        assertEquals(3L, (r[0][2] as Number).longValue(), "CT-MAP-097c; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_097" } catch (Exception ignore) {}
    }
}
