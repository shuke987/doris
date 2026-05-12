// CT-ARRAY-050: LIGHT_SCHEMA_CHANGE + ARRAY ADD/DROP
suite("repro_ct_array_050") {
    sql "DROP TABLE IF EXISTS t_ct_array_050"
    try {
        sql """
            CREATE TABLE t_ct_array_050 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_array_050 ADD COLUMN a ARRAY<INT>"
        sql "ALTER TABLE t_ct_array_050 DROP COLUMN a"
        def r = sql "DESC t_ct_array_050"
        assertTrue(r.size() >= 1, "CT-ARRAY-050: light_schema_change ADD/DROP ARRAY works; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_050" } catch (Exception ignore) {}
    }
}
