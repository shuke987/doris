suite("repro_ct_map_037") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_037"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_037 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_repro_ct_map_037 DROP COLUMN m"
        def r = sql "DESC t_repro_ct_map_037"
        boolean found = false
        for (def row : r) { if (row[0].toString().toLowerCase() == "m") found = true }
        assertFalse(found, "CT-MAP-037: DROP MAP success; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_037" } catch (Exception ignore) {}
    }
}
