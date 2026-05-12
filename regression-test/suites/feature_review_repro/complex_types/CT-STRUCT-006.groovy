suite("repro_ct_struct_006") {
    sql "DROP TABLE IF EXISTS t_ct_struct_006"
    try {
        sql """
            CREATE TABLE t_ct_struct_006 (id INT, s STRUCT<a:STRUCT<b:INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        assertTrue(true, "CT-STRUCT-006: nested STRUCT")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_006" } catch (Exception ignore) {}
    }
}
