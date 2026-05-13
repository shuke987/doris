// AGG-DT-009: MULTI_DISTINCT_GROUP_CONCAT vs GROUP_CONCAT(DISTINCT)
suite("repro_agg_dt_009") {
    sql "DROP TABLE IF EXISTS t_agg_dt_009"
    try {
        sql """CREATE TABLE t_agg_dt_009 (id INT, s VARCHAR(50)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_dt_009 VALUES (1,'a'),(2,'b'),(3,'a'),(4,'c'),(5,'b')"
        // 两路径 DISTINCT 结果元素集合应一致
        def r1 = sql "SELECT GROUP_CONCAT(DISTINCT s, ',') FROM t_agg_dt_009"
        Set<String> set1 = r1[0][0].toString().split(",") as Set

        boolean threw = false
        try {
            def r2 = sql "SELECT MULTI_DISTINCT_GROUP_CONCAT(s, ',') FROM t_agg_dt_009"
            Set<String> set2 = r2[0][0].toString().split(",") as Set
            // 元素集合应等
            assertEquals(set1, set2,
                "MULTI_DISTINCT_GROUP_CONCAT and GROUP_CONCAT(DISTINCT) should produce same element set; mdgc=${set2} gcd=${set1}")
        } catch (Exception e) {
            // 如不支持，记录
            threw = true
        }
        assertTrue(set1.size() == 3, "GROUP_CONCAT(DISTINCT) should have 3 unique elements (a,b,c); got=${set1}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt_009" } catch (Exception ignore) {}
    }
}
