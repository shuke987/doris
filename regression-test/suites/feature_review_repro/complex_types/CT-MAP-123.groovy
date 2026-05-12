suite("repro_ct_map_123") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_123"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_123 (id INT, m MAP<STRUCT<a:INT>, INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_123" } catch (Exception ignore) {} }
    // NEW-SEV-N15: spec says primitive only, current supportSubType=true allows
    assertTrue(threw, "CT-MAP-123: STRUCT MAP key should reject per spec §1.4 (NEW-SEV-N15); threw=${threw} err=${err}")
}
