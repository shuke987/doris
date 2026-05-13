// IIA-CUS-011: tokenizer=edge_ngram 前缀 (correct syntax: CREATE TOKENIZER first)
suite("repro_iia_cus_011") {
    sql "DROP TABLE IF EXISTS t_iia_cus_011"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_011"
    sql "DROP INVERTED INDEX TOKENIZER IF EXISTS iia_cus_011_tk"
    try {
        sql """CREATE INVERTED INDEX TOKENIZER IF NOT EXISTS iia_cus_011_tk PROPERTIES('type'='edge_ngram','min_gram'='2','max_gram'='4')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_cus_011 PROPERTIES('tokenizer'='iia_cus_011_tk')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_cus_011 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_cus_011'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_cus_011 VALUES (1,'doris')"
        // edge_ngram 2..4 → 'do','dor','dori' 是前缀 token
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_cus_011 WHERE c MATCH 'do'")[0][0],
                     "edge_ngram: MATCH 'do' should hit 'doris'")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_cus_011 WHERE c MATCH 'dori'")[0][0],
                     "edge_ngram: MATCH 'dori' should hit 'doris'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_cus_011" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_011" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX TOKENIZER IF EXISTS iia_cus_011_tk" } catch (Exception ignore) {}
    }
}
