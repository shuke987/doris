suite("repro_ct_index_001") {
    sql "DROP TABLE IF EXISTS t_ct_index_001"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_index_001 (id INT, arr ARRAY<STRING>, INDEX idx (arr) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_index_001" } catch (Exception ignore) {} }
    // spec: ARRAY<STRING> inverted index supported
    assertTrue(!threw, "CT-INDEX-001: inverted on ARRAY<STRING>; threw=${threw} err=${err}")
}
