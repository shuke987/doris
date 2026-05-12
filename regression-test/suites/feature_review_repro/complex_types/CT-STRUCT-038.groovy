suite("repro_ct_struct_038") {
    sql "DROP TABLE IF EXISTS t_ct_struct_038"
    try {
        sql """
            CREATE TABLE t_ct_struct_038 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_struct_038 ADD COLUMN s STRUCT<a:INT>"
        sql "ALTER TABLE t_ct_struct_038 DROP COLUMN s"
        assertTrue(true, "CT-STRUCT-038: light_schema_change STRUCT ADD/DROP")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_038" } catch (Exception ignore) {}
    }
}
