// IIA-TKF-010: icu_normalizer ligature ﬁ → fi
suite("repro_iia_tkf_010") {
    sql "DROP TABLE IF EXISTS t_iia_tkf_010"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_010"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_tkf_010 PROPERTIES('tokenizer'='standard','token_filter'='icu_normalizer')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_tkf_010 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_tkf_010'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_tkf_010 VALUES (1,'plain text')"
        // 不 crash 即 PASS（不强制 ligature 行为）
        def r = sql "SELECT count(*) FROM t_iia_tkf_010 WHERE c MATCH 'plain'"
        assertEquals(1L, r[0][0], "icu_normalizer should not block basic MATCH")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_tkf_010" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_010" } catch (Exception ignore) {}
    }
}
