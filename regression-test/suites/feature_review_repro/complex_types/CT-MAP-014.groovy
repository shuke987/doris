// CT-MAP-014: MAP value=BITMAP
suite("repro_ct_map_014") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_014"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_014 (id INT, m MAP<STRING,BITMAP>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_014" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-014: MAP value=BITMAP must reject; threw=${threw} err=${err}")
}
