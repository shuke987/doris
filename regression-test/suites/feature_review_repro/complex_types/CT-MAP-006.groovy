// CT-MAP-006: MAP value=STRUCT
suite("repro_ct_map_006") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_006"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_006 (id INT, m MAP<STRING,STRUCT<a:INT,b:STRING>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_006"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-006: MAP value=STRUCT; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_006" } catch (Exception ignore) {}
    }
}
