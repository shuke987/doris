suite("repro_ct_struct_004") {
    sql "DROP TABLE IF EXISTS t_ct_struct_004"
    try {
        sql """
            CREATE TABLE t_ct_struct_004 (id INT, s STRUCT<a:ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        assertTrue(true, "CT-STRUCT-004: STRUCT with ARRAY field")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_004" } catch (Exception ignore) {}
    }
}
