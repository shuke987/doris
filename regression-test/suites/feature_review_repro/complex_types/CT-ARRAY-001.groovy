// CT-ARRAY-001: 普通 ARRAY 列建表
suite("repro_ct_array_001") {
    sql "DROP TABLE IF EXISTS t_ct_array_001"
    try {
        sql """
            CREATE TABLE t_ct_array_001 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_ct_array_001"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "a") {
                if (row[1].toString().toLowerCase().contains("array")) found = true
            }
        }
        assertTrue(found, "CT-ARRAY-001: ARRAY<INT> column should exist; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_001" } catch (Exception ignore) {}
    }
}
