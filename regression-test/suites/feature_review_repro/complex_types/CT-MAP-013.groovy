// CT-MAP-013: MAP key=JSONB
suite("repro_ct_map_013") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_013"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_013 (id INT, m MAP<JSONB,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_013" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-013: MAP key=JSONB must reject; threw=${threw} err=${err}")
}
