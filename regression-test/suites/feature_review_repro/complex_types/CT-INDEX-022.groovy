suite("repro_ct_index_022") {
    sql "DROP TABLE IF EXISTS t_repro_ct_index_022"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_index_022 (id INT, c MAP<STRING,INT>, INDEX idx (c) USING BITMAP)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_index_022" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-INDEX-022: BITMAP on MAP reject; threw=${threw} err=${err}")
}
