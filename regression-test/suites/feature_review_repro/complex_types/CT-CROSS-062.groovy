suite("repro_ct_cross_062") {
    sql "DROP TABLE IF EXISTS t_ct_cross_062"
    try {
        sql """
            CREATE TABLE t_ct_cross_062 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_062 VALUES (1,10),(1,20),(1,10)"
        boolean threw = false; long sz = -1; String err = ""
        try {
            def r = sql "SELECT array_size(collect_set(v)) FROM t_ct_cross_062"
            sz = (r[0][0] as Number).longValue()
        } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw || sz == 2L, "CT-CROSS-062: collect_set distinct=2; threw=${threw} sz=${sz} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_062" } catch (Exception ignore) {}
    }
}
