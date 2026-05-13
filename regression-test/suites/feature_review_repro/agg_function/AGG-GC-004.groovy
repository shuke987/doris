// AGG-GC-004 (SEV-3 potential): GROUP_CONCAT(DISTINCT) 重排
// 实测发现：DISTINCT 改变 token 顺序 (hash-based 去重)
suite("repro_agg_gc_004") {
    sql "DROP TABLE IF EXISTS t_agg_gc_004"
    try {
        sql """
            CREATE TABLE t_agg_gc_004 (id INT, s VARCHAR(50))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // 重复项 + 不同顺序
        sql "INSERT INTO t_agg_gc_004 VALUES (1,'a'),(2,'b'),(3,'a'),(4,'c'),(5,'b')"
        // GROUP_CONCAT(DISTINCT) 去重但**不保证 order**
        def r = sql "SELECT GROUP_CONCAT(DISTINCT s) FROM t_agg_gc_004"
        String result = r[0][0].toString()
        // 元素正确（无重复 + 包含 a,b,c）
        Set<String> tokens = result.split(",") as Set
        assertEquals(3, tokens.size(), "DISTINCT 去重得 3 unique tokens; got=${result}")
        assertTrue(tokens.contains("a") && tokens.contains("b") && tokens.contains("c"),
                   "GROUP_CONCAT(DISTINCT) 含 a/b/c; got=${result}")
        // SEV-3 锚点：order **不**保证（不强断言具体顺序）
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_004" } catch (Exception ignore) {}
    }
}
