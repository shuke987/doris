// AGG-VAR-002 (N20): COUNT(DISTINCT variant_subcolumn) BE INTERNAL_ERROR
// FE 应拒绝 或 BE 应正确处理混合类型；当前 BE crash with "meet invalid type, type=Variant"
suite("repro_agg_var_002") {
    sql "DROP TABLE IF EXISTS t_agg_var_002"
    try {
        sql """CREATE TABLE t_agg_var_002 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_002 VALUES
            (1, '{"x": 100}'),
            (2, '{"x": "hello"}'),
            (3, '{"x": [1,2,3]}')"""
        boolean threw = false
        String msg = ""
        try {
            sql "SELECT COUNT(DISTINCT v['x']) FROM t_agg_var_002"
        } catch (Exception e) {
            threw = true
            msg = e.getMessage()
        }
        assertTrue(threw, "N20: COUNT(DISTINCT variant_subcol) should error (BE crash currently)")
        assertTrue(msg.toLowerCase().contains("variant") || msg.toLowerCase().contains("invalid type"),
            "Error should mention variant or invalid type (BE INTERNAL_ERROR); got=${msg}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_002" } catch (Exception ignore) {}
    }
}
