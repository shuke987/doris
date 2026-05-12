suite("repro_ct_struct_032") {
    sql "DROP TABLE IF EXISTS t_ct_struct_032"
    try {
        sql """
            CREATE TABLE t_ct_struct_032 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_struct_032 ADD COLUMN s STRUCT<a:INT>"
        def r = sql "DESC t_ct_struct_032"
        boolean found = false
        for (def row : r) { if (row[0].toString().toLowerCase() == "s") found = true }
        assertTrue(found, "CT-STRUCT-032: ADD STRUCT; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_032" } catch (Exception ignore) {}
    }
}
