suite("repro_ct_map_026") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_026"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_026 (id INT, m MAP<STRING,INT> DEFAULT '{}')
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_026" } catch (Exception ignore) {} }
    // spec: only NULL allowed for complex defaults
    assertTrue(threw, "CT-MAP-026: DEFAULT '{}' should reject (spec only NULL); threw=${threw} err=${err}")
}
