suite("repro_ct_index_023") {
    sql "DROP TABLE IF EXISTS t_repro_ct_index_023"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_index_023 (id INT, c ARRAY<STRING>, INDEX idx (c) USING NGRAM_BF PROPERTIES('gram_size'='2', 'bf_size'='1024'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_index_023" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-INDEX-023: ngram on ARRAY reject; threw=${threw} err=${err}")
}
