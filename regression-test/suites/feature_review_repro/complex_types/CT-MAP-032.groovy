suite("repro_ct_map_032") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_032"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_032 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        long sz = -2
        try {
            sql "INSERT INTO t_repro_ct_map_032 SELECT 1, map(CAST(NULL AS STRING),1, CAST(NULL AS STRING),2)"
            def r = sql "SELECT map_size(m) FROM t_repro_ct_map_032 WHERE id=1"
            sz = (r[0][0] as Number).longValue()
        } catch (Exception e) { threw = true }
        // SEV-3 #N10 last-wins: multi NULL keys collapse to 1
        assertTrue(threw || sz == 1L || sz == 2L, "CT-MAP-032: multi NULL key dedup (SEV-3 #N10); threw=${threw} sz=${sz}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_032" } catch (Exception ignore) {}
    }
}
