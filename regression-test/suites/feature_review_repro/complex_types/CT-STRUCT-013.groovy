suite("repro_ct_struct_013") {
    sql "DROP TABLE IF EXISTS t_ct_struct_013"
    try {
        sql """
            CREATE TABLE t_ct_struct_013 (id INT, s STRUCT<`字段`:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        assertTrue(true, "CT-STRUCT-013: chinese field name works")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_013" } catch (Exception ignore) {}
    }
}
