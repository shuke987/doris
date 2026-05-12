// CT-MAP-012: MAP key=HLL
suite("repro_ct_map_012") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_012"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_012 (id INT, m MAP<HLL,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_012" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-012: MAP key=HLL must reject; threw=${threw} err=${err}")
}
