// IIA-CRX-008: inverted_index_storage_format V2 + 自定义 analyzer
suite("repro_iia_crx_008") {
    sql "DROP TABLE IF EXISTS t_iia_crx_008"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_crx_008_an"
    try {
        sql """CREATE INVERTED INDEX ANALYZER iia_crx_008_an PROPERTIES('tokenizer'='standard','token_filter'='lowercase')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_crx_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_crx_008_an'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1', 'inverted_index_storage_format'='V2')
        """
        sql "INSERT INTO t_iia_crx_008 VALUES (1,'Hello WORLD')"
        def r = sql "SELECT count(*) FROM t_iia_crx_008 WHERE c MATCH 'hello'"
        assertEquals(1L, r[0][0], "V2 storage + custom analyzer should work")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_crx_008" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_crx_008_an" } catch (Exception ignore) {}
    }
}
