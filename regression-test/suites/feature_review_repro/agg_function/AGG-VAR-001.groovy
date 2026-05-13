// AGG-VAR-001: Variant subcolumn 混类型时 COUNT 与 SUM 一致性
// Spec (review doc + competitor):
//   Doris doc 说 implicit CAST required → 实际 cast 失败时静默 NULL → SUM 跳过
//   MongoDB $sum 同行为；Snowflake VARIANT 严格 (error)；BigQuery NULL per value
// 期望（合理 spec）：COUNT(v['x']) 与 effective SUM rows 一致；或 SUM 时若 cast 失败应给可检测信号
// 当前 4.1: COUNT(v['x'])=4 但 SUM 只算 1 行（数值），其他静默丢
suite("repro_agg_var_001") {
    sql "DROP TABLE IF EXISTS t_agg_var_001"
    try {
        sql """CREATE TABLE t_agg_var_001 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_001 VALUES
            (1, '{"x": 100}'),
            (2, '{"x": "hello"}'),
            (3, '{"x": [1,2,3]}'),
            (4, '{"x": {"nested": 42}}')"""
        // 期望: 如 COUNT 把非数值计了，SUM 也应有方式知道丢了；至少行为应一致
        // 严格 spec：mixed-type SUM 应报错或 COUNT 仅算数值（与 SUM 一致）
        def r = sql "SELECT COUNT(v['x']), SUM(v['x']) FROM t_agg_var_001"
        long cntVal = (long)r[0][0]
        def sumVal = r[0][1]
        // 期望：COUNT 应只算"可成功 cast 为数值"的行（与 SUM 一致）
        //   或 SUM 应该报错/警告
        // 当前 buggy: COUNT=4, SUM=100 — case 应 FAIL 直到统一
        assertEquals(1L, cntVal,
            "Variant subcolumn COUNT must match SUM's effective row count (only numeric-castable); current COUNT=${cntVal} but SUM only counts 1 numeric row")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_001" } catch (Exception ignore) {}
    }
}
