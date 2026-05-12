suite("repro_ct_map_036") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_036"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_036 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_repro_ct_map_036 ADD COLUMN m MAP<STRING,INT>"
        def r = sql "DESC t_repro_ct_map_036"
        boolean found = false
        for (def row : r) {
            if (row[0].toString().toLowerCase() == "m" && row[1].toString().toLowerCase().contains("map")) found = true
        }
        assertTrue(found, "CT-MAP-036: ADD MAP success; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_036" } catch (Exception ignore) {}
    }
}
