// AGG-VAR-005: Variant nested path 多层 SUM
suite("repro_agg_var_005") {
    sql "DROP TABLE IF EXISTS t_agg_var_005"
    try {
        sql """CREATE TABLE t_agg_var_005 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_005 VALUES
            (1, '{"a": {"b": {"c": 42}}}'),
            (2, '{"a": {"b": {"c": 100}}}'),
            (3, '{"a": {"b": {"c": 8}}}')"""
        def r = sql "SELECT SUM(v['a']['b']['c']) FROM t_agg_var_005"
        // 42+100+8 = 150
        assertEquals(150L, (long)r[0][0], "deep nested path SUM = 150")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_005" } catch (Exception ignore) {}
    }
}
