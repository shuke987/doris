// JT-CROSS-011: JSONB × JOIN
suite("repro_jt_cross_011") {
    sql "DROP TABLE IF EXISTS t_jt_cross_011_a"
    sql "DROP TABLE IF EXISTS t_jt_cross_011_b"
    try {
        sql """
            CREATE TABLE t_jt_cross_011_a (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_cross_011_b (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_cross_011_a VALUES (1,'{\"a\":1}'),(2,'{\"a\":2}')"
        sql "INSERT INTO t_jt_cross_011_b VALUES (1,10),(2,20)"
        def r = sql "SELECT t_jt_cross_011_a.j, t_jt_cross_011_b.v FROM t_jt_cross_011_a JOIN t_jt_cross_011_b ON t_jt_cross_011_a.id=t_jt_cross_011_b.id ORDER BY t_jt_cross_011_b.v"
        assertEquals(2, r.size(), "JT-CROSS-011; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_cross_011_a" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_cross_011_b" } catch (Exception ignore) {}
    }
}
