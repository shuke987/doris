suite("repro_ct_map_029") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_029"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_029 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "SHOW CREATE TABLE t_repro_ct_map_029"
        String s = r[0][1].toString().toLowerCase()
        assertTrue(s.contains("map<"), "CT-MAP-029: SHOW CREATE contains MAP<; observed=${s}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_029" } catch (Exception ignore) {}
    }
}
