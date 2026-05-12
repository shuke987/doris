suite("repro_ct_struct_024") {
    sql "DROP TABLE IF EXISTS t_ct_struct_024"
    try {
        sql """
            CREATE TABLE t_ct_struct_024 (id INT, s STRUCT<a:INT> NOT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_024 SELECT 1, named_struct('a', 1)"
        def r = sql "SELECT s FROM t_ct_struct_024 WHERE id=1"
        assertEquals(1, r.size(), "CT-STRUCT-024: NOT NULL STRUCT; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_024" } catch (Exception ignore) {}
    }
}
