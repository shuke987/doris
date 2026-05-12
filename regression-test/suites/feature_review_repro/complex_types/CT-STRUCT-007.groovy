suite("repro_ct_struct_007") {
    sql "DROP TABLE IF EXISTS t_ct_struct_007"
    try {
        sql """
            CREATE TABLE t_ct_struct_007 (id INT, s STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:INT>>>>>>>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        assertTrue(true, "CT-STRUCT-007: 9-level STRUCT works")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_007" } catch (Exception ignore) {}
    }
}
