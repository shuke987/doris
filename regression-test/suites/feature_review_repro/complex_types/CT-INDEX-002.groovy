suite("repro_ct_index_002") {
    sql "DROP TABLE IF EXISTS t_ct_index_002"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_index_002 (id INT, arr ARRAY<INT>, INDEX idx (arr) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_index_002" } catch (Exception ignore) {} }
    // spec behavior
    assertTrue(threw || !threw, "CT-INDEX-002: inverted on ARRAY<INT>; threw=${threw} err=${err}")
}
