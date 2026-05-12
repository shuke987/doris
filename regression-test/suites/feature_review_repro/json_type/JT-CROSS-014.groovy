// JT-CROSS-014: JSONB × INSERT INTO SELECT
suite("repro_jt_cross_014") {
    sql "DROP TABLE IF EXISTS t_jt_cross_014_src"
    sql "DROP TABLE IF EXISTS t_jt_cross_014_dst"
    try {
        sql """
            CREATE TABLE t_jt_cross_014_src (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_cross_014_dst (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_014_src VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}')"
        sql "INSERT INTO t_jt_cross_014_dst SELECT * FROM t_jt_cross_014_src"
        def r = sql "SELECT count(*) FROM t_jt_cross_014_dst"
        assertEquals("2", r[0][0].toString(), "JT-CROSS-014; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_014_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_cross_014_dst" } catch (Exception ignore) {}
    }
}
