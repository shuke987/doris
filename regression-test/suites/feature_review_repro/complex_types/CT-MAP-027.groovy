suite("repro_ct_map_027") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_027"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_027 (id INT, m MAP<STRING,INT> NOT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "INSERT INTO t_repro_ct_map_027 VALUES (1, NULL)" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_027" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-027: NOT NULL MAP + INSERT NULL reject; threw=${threw} err=${err}")
}
