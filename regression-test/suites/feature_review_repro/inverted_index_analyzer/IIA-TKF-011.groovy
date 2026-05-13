// IIA-TKF-011: token_filter 链 lowercase + asciifolding
suite("repro_iia_tkf_011") {
    sql "DROP TABLE IF EXISTS t_iia_tkf_011"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_011"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_tkf_011 PROPERTIES('tokenizer'='standard','token_filter'='lowercase, asciifolding')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_tkf_011 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_tkf_011'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_tkf_011 VALUES (1,'CAFÉ')"
        // 链式 lowercase + asciifolding → MATCH 'cafe' should hit
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_tkf_011 WHERE c MATCH 'cafe'")[0][0],
                     "chain lowercase+asciifolding: MATCH 'cafe' should hit 'CAFÉ'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_tkf_011" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_011" } catch (Exception ignore) {}
    }
}
