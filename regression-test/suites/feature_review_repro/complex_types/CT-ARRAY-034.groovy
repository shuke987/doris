// CT-ARRAY-034: DESC table ARRAY column type display
suite("repro_ct_array_034") {
    sql "DROP TABLE IF EXISTS t_ct_array_034"
    try {
        sql """
            CREATE TABLE t_ct_array_034 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_ct_array_034"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "a") {
                if (row[1].toString().toLowerCase().contains("array<int")) found = true
            }
        }
        assertTrue(found, "CT-ARRAY-034: DESC must show ARRAY<INT>; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_034" } catch (Exception ignore) {}
    }
}
