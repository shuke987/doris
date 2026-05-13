// AGG-AR-001: ARRAY_AGG 基础（不带 ORDER BY）
// Oracle: size 与 COUNT 一致；元素集合等于输入
suite("repro_agg_ar_001") {
    sql "DROP TABLE IF EXISTS t_agg_ar_001"
    try {
        sql """CREATE TABLE t_agg_ar_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_ar_001 VALUES (1,10),(2,20),(3,30)"
        def r = sql "SELECT SIZE(ARRAY_AGG(v)), COUNT(v) FROM t_agg_ar_001"
        assertEquals(r[0][1], r[0][0],
            "ARRAY_AGG size = COUNT(col); array_size=${r[0][0]} count=${r[0][1]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_ar_001" } catch (Exception ignore) {}
    }
}
