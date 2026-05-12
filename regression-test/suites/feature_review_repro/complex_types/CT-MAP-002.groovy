// CT-MAP-002: MAP<INT,STRING>
suite("repro_ct_map_002") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_002"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_002 (id INT, m MAP<INT,STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_002"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-002: MAP<INT,STRING>; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_002" } catch (Exception ignore) {}
    }
}
