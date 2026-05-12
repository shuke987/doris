suite("repro_ct_cross_064") {
    sql "DROP TABLE IF EXISTS t_ct_cross_064"
    try {
        sql """
            CREATE TABLE t_ct_cross_064 (id INT, k STRING, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_064 VALUES (1,'a',1),(1,'a',2)"
        boolean threw = false; long sz = -1; String err = ""
        try {
            def r = sql "SELECT map_size(map_agg(k, v)) FROM t_ct_cross_064"
            sz = (r[0][0] as Number).longValue()
        } catch (Exception e) { threw = true; err = e.toString() }
        // dedup keys
        assertTrue(threw || sz == 1L, "CT-CROSS-064: map_agg dup key dedup; threw=${threw} sz=${sz} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_064" } catch (Exception ignore) {}
    }
}
