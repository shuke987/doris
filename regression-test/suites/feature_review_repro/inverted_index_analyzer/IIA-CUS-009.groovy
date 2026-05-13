// IIA-CUS-009: tokenizer=ngram with min_gram/max_gram (correct syntax: CREATE TOKENIZER first)
suite("repro_iia_cus_009") {
    sql "DROP TABLE IF EXISTS t_iia_cus_009"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_009"
    sql "DROP INVERTED INDEX TOKENIZER IF EXISTS iia_cus_009_tk"
    try {
        sql """CREATE INVERTED INDEX TOKENIZER IF NOT EXISTS iia_cus_009_tk PROPERTIES('type'='ngram','min_gram'='2','max_gram'='3')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_cus_009 PROPERTIES('tokenizer'='iia_cus_009_tk')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_cus_009 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_cus_009'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_cus_009 VALUES (1,'abcd')"
        // ngram min=2 max=3 → 'ab','abc','bc','bcd','cd' 都是 token，所以 MATCH 任一应命中
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_cus_009 WHERE c MATCH 'ab'")[0][0],
                     "ngram min=2: MATCH 'ab' should hit 'abcd'")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_cus_009 WHERE c MATCH 'abc'")[0][0],
                     "ngram max=3: MATCH 'abc' should hit 'abcd'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_cus_009" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_009" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX TOKENIZER IF EXISTS iia_cus_009_tk" } catch (Exception ignore) {}
    }
}
