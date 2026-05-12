// JT-CROSS-010: JSONB × LIMIT / OFFSET
suite("repro_jt_cross_010") {
    sql "DROP TABLE IF EXISTS t_jt_cross_010"
    try {
        sql """
            CREATE TABLE t_jt_cross_010 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def vals = (1..10).collect { "(${it},'{\"a\":${it}}')" }.join(",")
        sql "INSERT INTO t_jt_cross_010 VALUES ${vals}"
        def r = sql "SELECT id FROM t_jt_cross_010 ORDER BY id LIMIT 3 OFFSET 5"
        assertEquals(3, r.size(), "JT-CROSS-010; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_010" } catch (Exception ignore) {}
    }
}
