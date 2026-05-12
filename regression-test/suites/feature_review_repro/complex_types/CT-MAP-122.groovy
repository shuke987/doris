suite("repro_ct_map_122") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_122"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_122 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_122 SELECT 1, map(':a:', 1)"
        def r = sql "SHOW CREATE TABLE t_repro_ct_map_122"
        // NEW-SEV-N16: MapLiteral.toSqlImpl doesn't escape ':'; round-trip may break
        String s = r[0][1].toString()
        assertTrue(s.length() > 0, "CT-MAP-122: SHOW CREATE no crash (NEW-SEV-N16 doc); observed length=${s.length()}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_122" } catch (Exception ignore) {}
    }
}
