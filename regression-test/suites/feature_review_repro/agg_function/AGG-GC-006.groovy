// AGG-GC-006: GROUP_CONCAT 含空串 '' 保留，NULL 跳过
suite("repro_agg_gc_006") {
    sql "DROP TABLE IF EXISTS t_agg_gc_006"
    try {
        sql """CREATE TABLE t_agg_gc_006 (id INT, s VARCHAR(50)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_gc_006 VALUES (1,'a'),(2,NULL),(3,''),(4,'b')"
        def r = sql "SELECT GROUP_CONCAT(s, '|' ORDER BY id) FROM t_agg_gc_006"
        // NULL 跳，'' 保留 → "a||b"
        assertEquals("a||b", r[0][0],
            "GROUP_CONCAT: NULL skipped, '' kept; got=${r[0][0]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_006" } catch (Exception ignore) {}
    }
}
