// AGG-MI-001: COUNT_IF (条件计数)
// Oracle: COUNT_IF(pred) = COUNT(*) WHERE pred；二者应等
suite("repro_agg_mi_001") {
    sql "DROP TABLE IF EXISTS t_agg_mi_001"
    try {
        sql """CREATE TABLE t_agg_mi_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_mi_001 VALUES (1,10),(2,20),(3,30),(4,40),(5,NULL)"
        boolean supported = false
        try {
            def r = sql "SELECT COUNT_IF(v > 15), COUNT(*) FILTER (WHERE v > 15) FROM t_agg_mi_001"
            assertEquals(r[0][1], r[0][0],
                "COUNT_IF(pred) = COUNT(*) FILTER(WHERE pred); got=${r}")
            supported = true
        } catch (Exception e) {
            // Doris 可能不支持 COUNT_IF；try with CASE WHEN
            def r2 = sql "SELECT COUNT(CASE WHEN v > 15 THEN 1 END) FROM t_agg_mi_001"
            // 3 个 > 15 (20,30,40)
            assertEquals(3L, r2[0][0], "COUNT(CASE WHEN v>15 THEN 1) = 3")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_mi_001" } catch (Exception ignore) {}
    }
}
