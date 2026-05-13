// AGG-GC-001: GROUP_CONCAT 默认分隔符 + NULL 处理
// Oracle: spec — NULL 跳过，'' 保留（with separator）
suite("repro_agg_gc_001") {
    sql "DROP TABLE IF EXISTS t_agg_gc_001"
    try {
        sql """
            CREATE TABLE t_agg_gc_001 (id INT, s VARCHAR(50))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_gc_001 VALUES (1,'a'),(2,'b'),(3,NULL),(4,'c')"
        def r = sql "SELECT GROUP_CONCAT(s ORDER BY id) FROM t_agg_gc_001"
        // ORDER BY id 锁顺序，NULL 跳过 → 'a,b,c'
        assertEquals("a,b,c", r[0][0],
            "GROUP_CONCAT skip NULL with default ',' separator + ORDER BY id")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_001" } catch (Exception ignore) {}
    }
}
