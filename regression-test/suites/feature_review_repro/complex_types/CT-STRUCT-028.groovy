suite("repro_ct_struct_028") {
    sql "DROP TABLE IF EXISTS t_ct_struct_028"
    try {
        sql """
            CREATE TABLE t_ct_struct_028 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_028 VALUES (1, NULL)"
        def r = sql "SELECT s FROM t_ct_struct_028 WHERE id=1"
        assertEquals(null, r[0][0], "CT-STRUCT-028: NULL struct; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_028" } catch (Exception ignore) {}
    }
}
