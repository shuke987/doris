// CT-ARRAY-041: ADD COLUMN ARRAY<INT> value
suite("repro_ct_array_041") {
    sql "DROP TABLE IF EXISTS t_ct_array_041"
    try {
        sql """
            CREATE TABLE t_ct_array_041 (id INT, x INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_array_041 ADD COLUMN a ARRAY<INT>"
        def r = sql "DESC t_ct_array_041"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "a" && row[1].toString().toLowerCase().contains("array")) found = true
        }
        assertTrue(found, "CT-ARRAY-041: ADD COLUMN ARRAY<INT> success; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_041" } catch (Exception ignore) {}
    }
}
