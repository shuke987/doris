suite("repro_ct_struct_005") {
    sql "DROP TABLE IF EXISTS t_ct_struct_005"
    try {
        sql """
            CREATE TABLE t_ct_struct_005 (id INT, s STRUCT<a:MAP<STRING,INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        assertTrue(true, "CT-STRUCT-005: STRUCT with MAP field")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_005" } catch (Exception ignore) {}
    }
}
