// AGG-NULL-001 (validation regression — N14 假设被证伪)
// 实测: COUNT(NULL literal) 实际返回 0 (SQL 标准正确)，COUNT(non-null literal) 返行数 = COUNT(*)
// review agent 的 N14 假设错（Count.java isCountStar() 不是完整路径）
suite("repro_agg_null_001") {
    sql "DROP TABLE IF EXISTS t_agg_null_001"
    try {
        sql """CREATE TABLE t_agg_null_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_null_001 VALUES (1,10),(2,20),(3,30)"
        // SQL 标准: COUNT(NULL) = 0
        def r1 = sql "SELECT COUNT(NULL) FROM t_agg_null_001"
        assertEquals(0L, r1[0][0], "COUNT(NULL literal) = 0 per SQL standard")

        // COUNT(*) 计行
        def r2 = sql "SELECT COUNT(*) FROM t_agg_null_001"
        assertEquals(3L, r2[0][0], "COUNT(*) = total rows")

        // COUNT(non-null literal) 视为 COUNT(*)
        def r3 = sql "SELECT COUNT(0) FROM t_agg_null_001"
        assertEquals(3L, r3[0][0], "COUNT(0) = COUNT(*) (literal is non-NULL)")

        def r4 = sql "SELECT COUNT(1) FROM t_agg_null_001"
        assertEquals(3L, r4[0][0], "COUNT(1) = COUNT(*)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_null_001" } catch (Exception ignore) {}
    }
}
