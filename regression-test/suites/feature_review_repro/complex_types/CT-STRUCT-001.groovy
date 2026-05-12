suite("repro_ct_struct_001") {
    sql "DROP TABLE IF EXISTS t_ct_struct_001"
    try {
        sql """
            CREATE TABLE t_ct_struct_001 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_ct_struct_001"
        boolean found = false
        for (def row : r) { if (row[0].toString().toLowerCase() == "s" && row[1].toString().toLowerCase().contains("struct")) found = true }
        assertTrue(found, "CT-STRUCT-001: STRUCT col; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_001" } catch (Exception ignore) {}
    }
}
