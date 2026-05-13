// AGG-AV-002: ANY_VALUE empty + all-NULL
suite("repro_agg_av_002") {
    sql "DROP TABLE IF EXISTS t_agg_av_002"
    try {
        sql """CREATE TABLE t_agg_av_002 (id INT, v INT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_av_002 VALUES (1, NULL),(2, NULL)"
        def r1 = sql "SELECT ANY_VALUE(v) FROM t_agg_av_002"
        assertEquals(null, r1[0][0], "ANY_VALUE(all NULL) = NULL")

        def r2 = sql "SELECT ANY_VALUE(v) FROM t_agg_av_002 WHERE 1=0"
        assertEquals(null, r2[0][0], "ANY_VALUE(empty) = NULL")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_av_002" } catch (Exception ignore) {}
    }
}
