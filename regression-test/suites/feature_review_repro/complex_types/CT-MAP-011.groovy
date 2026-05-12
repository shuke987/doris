// CT-MAP-011: MAP key=BITMAP
suite("repro_ct_map_011") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_011"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_011 (id INT, m MAP<BITMAP,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_011" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-011: MAP key=BITMAP must reject; threw=${threw} err=${err}")
}
