// CT-MAP-001: 普通 MAP
suite("repro_ct_map_001") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_001"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_001 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_001"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-001: 普通 MAP; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_001" } catch (Exception ignore) {}
    }
}
