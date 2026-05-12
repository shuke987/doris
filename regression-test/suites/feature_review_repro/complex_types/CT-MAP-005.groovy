// CT-MAP-005: MAP value=MAP
suite("repro_ct_map_005") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_005"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_005 (id INT, m MAP<STRING,MAP<STRING,INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_005"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-005: MAP value=MAP; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_005" } catch (Exception ignore) {}
    }
}
