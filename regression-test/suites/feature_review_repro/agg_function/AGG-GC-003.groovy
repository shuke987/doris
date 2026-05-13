// AGG-GC-003: GROUP_CONCAT ORDER BY 稳定性
suite("repro_agg_gc_003") {
    sql "DROP TABLE IF EXISTS t_agg_gc_003"
    try {
        sql """
            CREATE TABLE t_agg_gc_003 (id INT, s VARCHAR(50))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_gc_003 VALUES (1,'c'),(2,'a'),(3,'b')"
        // ORDER BY s ascending
        def r1 = sql "SELECT GROUP_CONCAT(s, ',' ORDER BY s) FROM t_agg_gc_003"
        assertEquals("a,b,c", r1[0][0], "ORDER BY s ascending → sorted alphabetically")

        // ORDER BY s DESC
        def r2 = sql "SELECT GROUP_CONCAT(s, ',' ORDER BY s DESC) FROM t_agg_gc_003"
        assertEquals("c,b,a", r2[0][0], "ORDER BY s DESC → reverse order")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_003" } catch (Exception ignore) {}
    }
}
