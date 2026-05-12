suite("repro_ct_struct_020") {
    sql "DROP TABLE IF EXISTS t_ct_struct_020"
    try {
        sql """
            CREATE TABLE t_ct_struct_020 (id INT, s STRUCT<a:INT, b:STRING> REPLACE)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_020 SELECT 1, named_struct('a',1,'b','x')"
        def r = sql "SELECT s FROM t_ct_struct_020 WHERE id=1"
        assertEquals(1, r.size(), "CT-STRUCT-020: AGG REPLACE STRUCT; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_020" } catch (Exception ignore) {}
    }
}
