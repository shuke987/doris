suite("repro_ct_struct_033") {
    sql "DROP TABLE IF EXISTS t_ct_struct_033"
    try {
        sql """
            CREATE TABLE t_ct_struct_033 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_struct_033 DROP COLUMN s"
        def r = sql "DESC t_ct_struct_033"
        boolean found = false
        for (def row : r) { if (row[0].toString().toLowerCase() == "s") found = true }
        assertFalse(found, "CT-STRUCT-033: DROP STRUCT; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_033" } catch (Exception ignore) {}
    }
}
