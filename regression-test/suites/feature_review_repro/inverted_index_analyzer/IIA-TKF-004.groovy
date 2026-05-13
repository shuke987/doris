// IIA-TKF-004: asciifolding (é → e)
suite("repro_iia_tkf_004") {
    sql "DROP TABLE IF EXISTS t_iia_tkf_004"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_004"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_tkf_004 PROPERTIES('tokenizer'='standard','token_filter'='lowercase, asciifolding')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_tkf_004 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_tkf_004'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_tkf_004 VALUES (1,'café'),(2,'plain')"
        // asciifolding 应使 'cafe' 命中 'café'
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_tkf_004 WHERE c MATCH 'cafe'")[0][0],
                     "asciifolding: MATCH 'cafe' should hit 'café'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_tkf_004" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_004" } catch (Exception ignore) {}
    }
}
