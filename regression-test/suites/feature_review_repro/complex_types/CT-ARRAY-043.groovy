// CT-ARRAY-043: DROP COLUMN ARRAY<INT>
suite("repro_ct_array_043") {
    sql "DROP TABLE IF EXISTS t_ct_array_043"
    try {
        sql """
            CREATE TABLE t_ct_array_043 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_array_043 DROP COLUMN a"
        def r = sql "DESC t_ct_array_043"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "a") found = true
        }
        assertFalse(found, "CT-ARRAY-043: DROP ARRAY column success; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_043" } catch (Exception ignore) {}
    }
}
