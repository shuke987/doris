suite("repro_ct_struct_067") {
    sql "DROP TABLE IF EXISTS t_ct_struct_067"
    try {
        sql """
            CREATE TABLE t_ct_struct_067 (id INT, s STRUCT<a:INT,b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_067 SELECT 1, named_struct('a',1,'b','x')"
        def r = sql "SELECT s.a FROM t_ct_struct_067 WHERE id=1"
        assertEquals(1, (r[0][0] as Number).intValue(), "CT-STRUCT-067: dot access; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_067" } catch (Exception ignore) {}
    }
}
