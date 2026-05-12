suite("repro_ct_cross_012") {
    sql "DROP TABLE IF EXISTS t_ct_cross_012"
    try {
        sql """
            CREATE TABLE t_ct_cross_012 (id INT, a ARRAY<INT>, m MAP<STRING,INT>, s STRUCT<x:INT,y:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_012 SELECT 1, array(1,2), map('a',1), named_struct('x',1,'y','hello')"
        def r = sql "SELECT array_size(a), map_size(m) FROM t_ct_cross_012 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-CROSS-012a; observed=${r}")
        assertEquals(1L, (r[0][1] as Number).longValue(), "CT-CROSS-012b; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_012" } catch (Exception ignore) {}
    }
}
