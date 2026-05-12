suite("repro_ct_index_004") {
    sql "DROP TABLE IF EXISTS t_ct_index_004"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_index_004 (id INT, s STRUCT<a:STRING>, INDEX idx (s) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_index_004" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-INDEX-004: inverted on STRUCT reject; threw=${threw} err=${err}")
}
