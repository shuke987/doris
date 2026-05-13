// AGG-GC-002: GROUP_CONCAT 自定义分隔符
suite("repro_agg_gc_002") {
    sql "DROP TABLE IF EXISTS t_agg_gc_002"
    try {
        sql """
            CREATE TABLE t_agg_gc_002 (id INT, s VARCHAR(50))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_gc_002 VALUES (1,'a'),(2,'b'),(3,'c')"
        def r = sql "SELECT GROUP_CONCAT(s, '|' ORDER BY id) FROM t_agg_gc_002"
        assertEquals("a|b|c", r[0][0], "GROUP_CONCAT with custom separator '|'")

        def r2 = sql "SELECT GROUP_CONCAT(s, '<>' ORDER BY id) FROM t_agg_gc_002"
        assertEquals("a<>b<>c", r2[0][0], "GROUP_CONCAT multi-char separator '<>'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_002" } catch (Exception ignore) {}
    }
}
