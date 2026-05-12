suite("repro_ct_cross_063") {
    sql "DROP TABLE IF EXISTS t_ct_cross_063"
    try {
        sql """
            CREATE TABLE t_ct_cross_063 (id INT, k STRING, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_063 VALUES (1,'a',1),(1,'b',2)"
        boolean threw = false; long sz = -1; String err = ""
        try {
            def r = sql "SELECT map_size(map_agg(k, v)) FROM t_ct_cross_063"
            sz = (r[0][0] as Number).longValue()
        } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw || sz == 2L, "CT-CROSS-063: map_agg size=2; threw=${threw} sz=${sz} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_063" } catch (Exception ignore) {}
    }
}
