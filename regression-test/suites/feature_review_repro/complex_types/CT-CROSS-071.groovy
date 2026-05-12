suite("repro_ct_cross_071") {
    sql "DROP TABLE IF EXISTS t_ct_cross_071"
    try {
        sql """
            CREATE TABLE t_ct_cross_071 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_cross_071 ADD COLUMN arr ARRAY<INT>"
        assertTrue(true, "CT-CROSS-071: light ADD ARRAY")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_071" } catch (Exception ignore) {}
    }
}
