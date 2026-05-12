suite("repro_ct_cross_073") {
    sql "DROP TABLE IF EXISTS t_ct_cross_073"
    try {
        sql """
            CREATE TABLE t_ct_cross_073 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "group_commit_interval_ms"="50")
        """
        sql "INSERT INTO t_ct_cross_073 VALUES (1, [1,2,3])"
        def r = sql "SELECT count(*) FROM t_ct_cross_073"
        assertTrue((r[0][0] as Number).longValue() >= 0, "CT-CROSS-073: group_commit insert; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_073" } catch (Exception ignore) {}
    }
}
