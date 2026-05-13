// IIA-CHF-008: char_filter=icu_normalizer via custom analyzer
suite("repro_iia_chf_008") {
    sql "DROP TABLE IF EXISTS t_iia_chf_008"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_chf_008"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_chf_008 PROPERTIES('tokenizer'='standard','char_filter'='icu_normalizer')"""
        Thread.sleep(10000)  // wait FE→BE policy sync
        sql """
            CREATE TABLE t_iia_chf_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_chf_008'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_chf_008 VALUES (1,'plain')"
        def r = sql "SELECT count(*) FROM t_iia_chf_008 WHERE c MATCH 'plain'"
        assertEquals(1L, r[0][0], "icu_normalizer char_filter should not block basic MATCH")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_chf_008" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_chf_008" } catch (Exception ignore) {}
    }
}
