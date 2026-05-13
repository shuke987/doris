// IIA-TKF-005: word_delimiter (Wi-Fi → Wi, Fi)
suite("repro_iia_tkf_005") {
    sql "DROP TABLE IF EXISTS t_iia_tkf_005"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_005"
    sql "DROP INVERTED INDEX TOKEN_FILTER IF EXISTS iia_tkf_005_wd"
    try {
        sql """CREATE INVERTED INDEX TOKEN_FILTER IF NOT EXISTS iia_tkf_005_wd PROPERTIES('type'='word_delimiter')"""
        Thread.sleep(10000)
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_tkf_005 PROPERTIES('tokenizer'='standard','token_filter'='iia_tkf_005_wd, lowercase')"""
        Thread.sleep(10000)
        sql """
            CREATE TABLE t_iia_tkf_005 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_tkf_005'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_tkf_005 VALUES (1,'Wi-Fi power-up')"
        // word_delimiter 拆分 hyphen → 'wi' 应命中
        def r = sql "SELECT count(*) FROM t_iia_tkf_005 WHERE c MATCH 'wi'"
        assertEquals(1L, r[0][0], "word_delimiter: MATCH 'wi' should hit 'Wi-Fi'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_tkf_005" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_005" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX TOKEN_FILTER IF EXISTS iia_tkf_005_wd" } catch (Exception ignore) {}
    }
}
