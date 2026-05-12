suite("repro_ct_map_024") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_024"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_024 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(m) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_024" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-024: MAP DISTRIBUTED BY HASH reject; threw=${threw} err=${err}")
}
