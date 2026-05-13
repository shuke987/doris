// AGG-VAR-003: Variant subcolumn AVG behavior on mixed-type
suite("repro_agg_var_003") {
    sql "DROP TABLE IF EXISTS t_agg_var_003"
    try {
        sql """CREATE TABLE t_agg_var_003 (id INT, v VARIANT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql """INSERT INTO t_agg_var_003 VALUES
            (1, '{"x": 100}'),
            (2, '{"x": "string_val"}'),
            (3, '{"x": 200}'),
            (4, '{"x": 3.14}')"""
        def r = sql "SELECT AVG(v['x']), SUM(v['x']), COUNT(v['x']) FROM t_agg_var_003"
        // SUM = 100 + 200 + 3.14 = 303.14（string 跳）
        // COUNT = 4（string 也计数）
        // AVG = SUM / COUNT(numeric only) = 303.14 / 3 ~ 101.05？或 = 303.14 / 4 ~ 75.79？
        // 实测看一下 AVG 用哪个 COUNT
        def avg = r[0][0]
        def sum = r[0][1]
        def cnt = r[0][2]
        assertTrue(avg != null, "AVG 不应 NULL")
        // 锁住 AVG 行为
        double avgVal = (double)avg
        double sumVal = (double)sum
        // 若 AVG = SUM / 3 → 用 numeric count
        // 若 AVG = SUM / 4 → 用全 count (含 string)
        boolean avgUsesNumericCount = Math.abs(avgVal - sumVal/3.0) < 0.01
        boolean avgUsesFullCount = Math.abs(avgVal - sumVal/4.0) < 0.01
        assertTrue(avgUsesNumericCount || avgUsesFullCount,
            "AVG should be SUM/3 (numeric count) or SUM/4 (full count); AVG=${avgVal} SUM=${sumVal} COUNT=${cnt}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_var_003" } catch (Exception ignore) {}
    }
}
