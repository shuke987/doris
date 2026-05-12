// CT-MAP-003: MAP<DECIMAL,DATE>
suite("repro_ct_map_003") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_003"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_003 (id INT, m MAP<DECIMAL(10,2),DATE>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_003"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-003: MAP<DECIMAL,DATE>; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_003" } catch (Exception ignore) {}
    }
}
