suite("repro_ct_map_023") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_023"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_023 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id)
            PARTITION BY LIST(m) (PARTITION p1 VALUES IN (('{"a":1}'))) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_023" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-023: MAP LIST PARTITION reject; threw=${threw} err=${err}")
}
