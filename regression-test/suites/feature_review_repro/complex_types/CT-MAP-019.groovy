suite("repro_ct_map_019") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_019"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_019 (m MAP<STRING,INT>, b INT)
            AGGREGATE KEY(m) DISTRIBUTED BY HASH(b) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_019" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-019: MAP AGGREGATE key reject; threw=${threw} err=${err}")
}
