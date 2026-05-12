// JT-CROSS-044: JSONB 列 INSERT 后 OPTIMIZE / COMPACT-friendly read
suite("repro_jt_cross_044") {
    sql "DROP TABLE IF EXISTS t_jt_cross_044"
    try {
        sql """
            CREATE TABLE t_jt_cross_044 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // multiple inserts to create multiple rowsets
        (1..5).each {
            int x = it
            sql "INSERT INTO t_jt_cross_044 VALUES (${x},'{\"a\":${x}}')"
        }
        def r = sql "SELECT count(*) FROM t_jt_cross_044"
        assertEquals("5", r[0][0].toString(), "JT-CROSS-044; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_044" } catch (Exception ignore) {}
    }
}
