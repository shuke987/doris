// AGG-AV-001: ANY_VALUE 任一非 NULL 值
suite("repro_agg_av_001") {
    sql "DROP TABLE IF EXISTS t_agg_av_001"
    try {
        sql """CREATE TABLE t_agg_av_001 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_av_001 VALUES (1,10),(2,20),(3,30)"
        def r = sql "SELECT ANY_VALUE(v) FROM t_agg_av_001"
        // 必须是输入之一
        int val = (int)r[0][0]
        assertTrue(val == 10 || val == 20 || val == 30,
            "ANY_VALUE returns one of input values; got=${val}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_av_001" } catch (Exception ignore) {}
    }
}
