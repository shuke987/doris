suite("repro_ct_map_017") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_017"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_017 (m MAP<STRING,INT>, b INT)
            DUPLICATE KEY(m) DISTRIBUTED BY HASH(b) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_017" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-017: MAP DUPLICATE key reject; threw=${threw} err=${err}")
}
