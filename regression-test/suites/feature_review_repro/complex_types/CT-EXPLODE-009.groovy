suite("repro_ct_explode_009") {
    sql "DROP TABLE IF EXISTS t_ct_explode_009"
    try {
        sql """
            CREATE TABLE t_ct_explode_009 (id INT, a1 ARRAY<INT>, a2 ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_009 VALUES (1, [1,2], [10,20,30])"
        def r = sql "SELECT count(*) FROM t_ct_explode_009 LATERAL VIEW explode(a1) tmp1 AS x LATERAL VIEW explode(a2) tmp2 AS y"
        assertEquals(6L, (r[0][0] as Number).longValue(), "CT-EXPLODE-009: 2x3=6 cartesian; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_009" } catch (Exception ignore) {}
    }
}
