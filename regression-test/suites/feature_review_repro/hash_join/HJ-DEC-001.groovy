// HJ-DEC-001 (SEV-1: HashJoin ON DECIMAL different scales 强制 CAST 到 (38,6) 丢精度)
// Spec: JOIN ON DECIMAL(38,2) = DECIMAL(38,10) 应 promote 到共同 (38, max(scale)=10),
//        否则精度损失.
// 当前 4.1: BE plan 走 expr_cast 到 DECIMAL(38, 6) 共同 scale, 高精度被截断.
//   200.25 (scale=2) cast→ 200.250000
//   200.2500000001 (scale=10) cast→ 200.250000  (丢 0.0000000001)
//   两者错误匹配, 用户精度数据被静默"对齐"成相等.
suite("repro_hj_dec_001") {
    sql "DROP TABLE IF EXISTS t_hj_dec_1"
    sql "DROP TABLE IF EXISTS t_hj_dec_2"
    try {
        sql """
            CREATE TABLE t_hj_dec_1 (id INT, v DECIMAL(38,2))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_hj_dec_2 (id INT, v DECIMAL(38,10))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_hj_dec_1 VALUES (1, 100.50), (2, 200.25), (3, 999.99)"
        sql "INSERT INTO t_hj_dec_2 VALUES (1, 100.5000000000), (2, 200.2500000001), (3, 999.99)"
        // 期望: id=1,3 匹配; id=2 不匹配 (200.25 != 200.2500000001 精确值)
        def r = sql "SELECT a.id FROM t_hj_dec_1 a JOIN t_hj_dec_2 b ON a.v = b.v ORDER BY a.id"
        def ids = r.collect { it[0].toString() }
        assertFalse(ids.contains("2"),
            "JOIN DECIMAL(38,2)=DECIMAL(38,10) must promote to (38,max(scale)=10) to preserve precision; " +
            "200.25 must NOT match 200.2500000001; got matched ids=${ids}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_hj_dec_1" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_hj_dec_2" } catch (Exception ignore) {}
    }
}
