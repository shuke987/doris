// AGG-VAR-004: Variant subcolumn 数值类型一致时 SUM 正确
// Spec: 若 subcolumn 在所有行都是数值类型，SUM 应等于显式 CAST 后的 SUM
suite("repro_agg_var_004") {
    sql "DROP TABLE IF EXISTS t_agg_var_004"
    try {
        sql """CREATE TABLE t_agg_var_004 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_004 VALUES
            (1, '{"price": 100}'),
            (2, '{"price": 200}'),
            (3, '{"price": 50}')"""
        def r = sql """SELECT SUM(v['price']), SUM(CAST(v['price'] AS BIGINT)) FROM t_agg_var_004"""
        // 同数值类型 → implicit CAST 与 explicit CAST 应等
        assertEquals(r[0][1], r[0][0],
            "Same numeric type → implicit CAST SUM == explicit CAST SUM; implicit=${r[0][0]} explicit=${r[0][1]}")
        // 350 = 100+200+50
        assertEquals(350L, (long)r[0][0], "SUM(v['price']) = 350")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_004" } catch (Exception ignore) {}
    }
}
