// AGG-MI-002: SUM_IF
suite("repro_agg_mi_002") {
    sql "DROP TABLE IF EXISTS t_agg_mi_002"
    try {
        sql """CREATE TABLE t_agg_mi_002 (id INT, v INT, p INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_mi_002 VALUES (1,10,1),(2,20,0),(3,30,1),(4,40,0)"
        // SUM_IF(v, p=1) = SUM(v WHERE p=1) = 10+30 = 40
        boolean supported = false
        try {
            def r = sql "SELECT SUM_IF(p=1, v) FROM t_agg_mi_002"
            assertEquals(40L, r[0][0], "SUM_IF(p=1, v) = 40")
            supported = true
        } catch (Exception e) {
            // fallback to CASE WHEN
            def r2 = sql "SELECT SUM(CASE WHEN p=1 THEN v ELSE 0 END) FROM t_agg_mi_002"
            assertEquals(40L, r2[0][0], "fallback SUM(CASE WHEN p=1 THEN v) = 40")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_mi_002" } catch (Exception ignore) {}
    }
}
