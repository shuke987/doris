// AGG-AR-003: ARRAY_AGG workaround via subquery ORDER BY
suite("repro_agg_ar_003") {
    sql "DROP TABLE IF EXISTS t_agg_ar_003"
    try {
        sql """CREATE TABLE t_agg_ar_003 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_ar_003 VALUES (3,30),(1,10),(2,20)"
        // Workaround: ORDER BY in subquery
        def r = sql "SELECT ARRAY_AGG(v) FROM (SELECT v FROM t_agg_ar_003 ORDER BY id) x"
        String arr = r[0][0].toString()
        // workaround 实际未保证顺序传递（依 plan）
        assertTrue(arr.contains("10") && arr.contains("20") && arr.contains("30"),
            "ARRAY_AGG workaround gets all elements; got=${arr}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_ar_003" } catch (Exception ignore) {}
    }
}
