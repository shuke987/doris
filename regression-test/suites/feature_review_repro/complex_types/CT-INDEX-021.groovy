suite("repro_ct_index_021") {
    sql "DROP TABLE IF EXISTS t_repro_ct_index_021"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_index_021 (id INT, c ARRAY<INT>, INDEX idx (c) USING BITMAP)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_index_021" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-INDEX-021: BITMAP on ARRAY reject; threw=${threw} err=${err}")
}
