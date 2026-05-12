suite("repro_ct_struct_002") {
    sql "DROP TABLE IF EXISTS t_ct_struct_002"
    try {
        sql """
            CREATE TABLE t_ct_struct_002 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_ct_struct_002"
        assertTrue(r.size() >= 2, "CT-STRUCT-002: 1-field STRUCT; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_002" } catch (Exception ignore) {}
    }
}
