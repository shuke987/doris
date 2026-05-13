// IIA-TKF-001: lowercase token filter via custom analyzer (use CREATE TABLE pattern, not tokenize())
suite("repro_iia_tkf_001") {
    sql "DROP TABLE IF EXISTS t_iia_tkf_001"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_001_lower"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_tkf_001_lower PROPERTIES('tokenizer'='standard','token_filter'='lowercase')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_tkf_001 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_tkf_001_lower'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_tkf_001 VALUES (1,'Hello WORLD'),(2,'foo')"
        // lowercase filter → MATCH 'world' should hit
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_tkf_001 WHERE c MATCH 'world'")[0][0],
                     "lowercase filter: MATCH 'world' should hit 'Hello WORLD'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_tkf_001" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_001_lower" } catch (Exception ignore) {}
    }
}
