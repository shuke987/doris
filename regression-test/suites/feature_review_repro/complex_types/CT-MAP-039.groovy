suite("repro_ct_map_039") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_039"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_039 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "ALTER TABLE t_repro_ct_map_039 MODIFY COLUMN m MAP<INT,STRING>" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_039" } catch (Exception ignore) {} }
    // spec: key type change should reject per matrix
    assertTrue(threw, "CT-MAP-039: MODIFY key type reject; threw=${threw} err=${err}")
}
