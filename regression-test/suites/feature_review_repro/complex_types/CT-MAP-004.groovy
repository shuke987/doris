// CT-MAP-004: MAP value=ARRAY
suite("repro_ct_map_004") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_004"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_004 (id INT, m MAP<STRING,ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_004"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-004: MAP value=ARRAY; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_004" } catch (Exception ignore) {}
    }
}
