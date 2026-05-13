// AGG-VAR-002 (N20 SEV-2): COUNT(DISTINCT variant_subcolumn) BE INTERNAL_ERROR
// Spec: DISTINCT 应基于类型 promotion 后的 JSONB binary 比较，或 FE 应清楚拒绝
// 当前 4.1: BE 抛 INTERNAL_ERROR "meet invalid type, type=Variant" — 真 bug
suite("repro_agg_var_002") {
    sql "DROP TABLE IF EXISTS t_agg_var_002"
    try {
        sql """CREATE TABLE t_agg_var_002 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_002 VALUES
            (1, '{"x": 100}'),
            (2, '{"x": "hello"}'),
            (3, '{"x": [1,2,3]}')"""
        // 期望（spec correct）：3 个 distinct 值（每行不同 JSONB-promoted 值）
        def r = sql "SELECT COUNT(DISTINCT v['x']) FROM t_agg_var_002"
        assertEquals(3L, r[0][0],
            "COUNT(DISTINCT variant_subcolumn) must work on mixed-type values via JSONB binary compare; current BE INTERNAL_ERROR is a real bug")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_002" } catch (Exception ignore) {}
    }
}
