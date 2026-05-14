// AGG-DT6-001 (R4-3: MAX(dt6) - MIN(dt6) 在 DATETIME(6) 上返 0, 丢 microsecond 精度)
// Spec: DATETIME(6) 保留 microsecond，MAX - MIN 应反映 microsecond 级差异。
// 当前 4.1: 减法 round 到 SECOND，sub-second 差返 0
//   两行 dt: 10:00:00.123456, 10:00:00.999999 (差 0.876543 秒 = 876543 微秒)
//   MAX(t) - MIN(t) 返 0 → 丢精度
suite("repro_agg_dt6_001") {
    sql "DROP TABLE IF EXISTS t_agg_dt6_001"
    try {
        sql """
            CREATE TABLE t_agg_dt6_001 (id INT, t DATETIME(6))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_dt6_001 VALUES (1, '2024-01-01 10:00:00.123456'), (2, '2024-01-01 10:00:00.999999')"
        def r = sql "SELECT MAX(t) - MIN(t) FROM t_agg_dt6_001"
        // 期望（非 0 — 反映 microsecond 差）:
        //   严格: 0.876543 (秒) 或 876543 (微秒)
        //   宽松: 非 0 任意值
        def diff = r[0][0].toString()
        assertNotEquals("0", diff,
            "MAX(dt6) - MIN(dt6) must reflect sub-second difference; got ${diff} for 0.876543s actual diff")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_dt6_001" } catch (Exception ignore) {}
    }
}
