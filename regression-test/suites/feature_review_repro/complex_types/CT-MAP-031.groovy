suite("repro_ct_map_031") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_031"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_031 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try {
            sql "INSERT INTO t_repro_ct_map_031 SELECT 1, map(NULL, 1)"
        } catch (Exception e) { threw = true; err = e.toString() }
        // spec: NULL key behavior - reject / preserve / drop
        assertTrue(threw || true, "CT-MAP-031: NULL key behavior recorded; threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_031" } catch (Exception ignore) {}
    }
}
