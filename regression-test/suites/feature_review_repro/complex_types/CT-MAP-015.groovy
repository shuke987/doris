suite("repro_ct_map_015") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_015"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_015 (id INT, m MAP<STRING, MAP<STRING, MAP<STRING, MAP<STRING, MAP<STRING, MAP<STRING, MAP<STRING, MAP<STRING, INT>>>>>>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_repro_ct_map_015"
        assertTrue(r.size() >= 2, "CT-MAP-015: 8-level MAP nesting (within 9-limit); desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_015" } catch (Exception ignore) {}
    }
}
