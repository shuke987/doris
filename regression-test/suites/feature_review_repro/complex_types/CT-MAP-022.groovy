suite("repro_ct_map_022") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_022"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_022 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id)
            PARTITION BY RANGE(m) (PARTITION p1 VALUES LESS THAN ('{"a":100}')) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_022" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-022: MAP RANGE PARTITION reject; threw=${threw} err=${err}")
}
