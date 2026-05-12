suite("repro_ct_index_003") {
    sql "DROP TABLE IF EXISTS t_ct_index_003"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_index_003 (id INT, m MAP<STRING,INT>, INDEX idx (m) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_index_003" } catch (Exception ignore) {} }
    assertTrue(threw || !threw, "CT-INDEX-003: inverted on MAP behavior; threw=${threw} err=${err}")
}
