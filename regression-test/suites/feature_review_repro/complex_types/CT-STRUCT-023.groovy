suite("repro_ct_struct_023") {
    sql "DROP TABLE IF EXISTS t_ct_struct_023"
    try {
        sql """
            CREATE TABLE t_ct_struct_023 (id INT, s STRUCT<a:INT> DEFAULT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_023 (id) VALUES (1)"
        def r = sql "SELECT s FROM t_ct_struct_023 WHERE id=1"
        assertEquals(null, r[0][0], "CT-STRUCT-023: DEFAULT NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_023" } catch (Exception ignore) {}
    }
}
