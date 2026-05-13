// AGG-VAR-001 (N19): Variant subcolumn SUM/COUNT 不一致 — 非数值静默丢
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
        // COUNT(v['x']) 数所有 non-null subcolumn = 4
        // SUM(v['x']) 只 sum 数值行 = 100
        def r = sql "SELECT COUNT(v['x']), SUM(v['x']) FROM t_agg_var_001"
        long cntVal = (long)r[0][0]
        // sum 可能是 Double / Decimal / Long depending on inference
        def sumVal = r[0][1]
        // BUG 复现：COUNT=4 但 SUM 只算数值行
        assertEquals(4L, cntVal, "COUNT(v['x']) counts ALL non-null subcolumn values")
        // SUM 应等于 100（只数值行）—— 锁当前 buggy 行为
        // 用 toString 比较因为 type 可能是 Double/Long
        String sumStr = sumVal.toString()
        assertTrue(sumStr.startsWith("100") || sumStr == "100.0",
            "BUG N19: SUM only counts numeric subcolumn rows (string/array/object silently dropped); SUM=${sumStr} COUNT=${cntVal} → COUNT ≠ effective SUM rows")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_001" } catch (Exception ignore) {}
    }
}
