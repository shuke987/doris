suite("repro_ct_map_040") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_040"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_040 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_repro_ct_map_040 ADD COLUMN m MAP<STRING,INT>"
        sql "ALTER TABLE t_repro_ct_map_040 DROP COLUMN m"
        assertTrue(true, "CT-MAP-040: light_schema_change MAP ADD/DROP works")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_040" } catch (Exception ignore) {}
    }
}
