// AGG-GC-005: GROUP_CONCAT empty group → NULL
suite("repro_agg_gc_005") {
    sql "DROP TABLE IF EXISTS t_agg_gc_005"
    try {
        sql """CREATE TABLE t_agg_gc_005 (id INT, s VARCHAR(50)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_gc_005 VALUES (1,'a')"
        def r = sql "SELECT GROUP_CONCAT(s) FROM t_agg_gc_005 WHERE 1=0"
        assertEquals(null, r[0][0], "GROUP_CONCAT(empty) = NULL")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_005" } catch (Exception ignore) {}
    }
}
