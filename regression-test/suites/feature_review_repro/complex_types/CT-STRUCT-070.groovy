suite("repro_ct_struct_070") {
    sql "DROP TABLE IF EXISTS t_ct_struct_070"
    try {
        sql """
            CREATE TABLE t_ct_struct_070 (id INT, s STRUCT<a:STRUCT<b:STRUCT<c:INT>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_070 SELECT 1, named_struct('a', named_struct('b', named_struct('c', 42)))"
        def r = sql "SELECT s.a.b.c FROM t_ct_struct_070 WHERE id=1"
        assertEquals(42, (r[0][0] as Number).intValue(), "CT-STRUCT-070: nested dot access; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_070" } catch (Exception ignore) {}
    }
}
