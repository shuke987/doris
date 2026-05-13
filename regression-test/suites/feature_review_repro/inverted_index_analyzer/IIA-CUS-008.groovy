// IIA-CUS-008: tokenizer=keyword → 整字段 token
suite("repro_iia_cus_008") {
    sql "DROP TABLE IF EXISTS t_iia_cus_008"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_008"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_cus_008 PROPERTIES('tokenizer'='keyword')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_cus_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_cus_008'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_cus_008 VALUES (1,'hello world')"
        // keyword = 整字段单 token → MATCH 'hello' 不命中
        assertEquals(0L, sql("SELECT count(*) FROM t_iia_cus_008 WHERE c MATCH 'hello'")[0][0],
                     "keyword tokenizer: substring MATCH should NOT hit (whole field is single token)")
        // MATCH 完整字段命中
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_cus_008 WHERE c MATCH 'hello world'")[0][0],
                     "keyword tokenizer: full-field MATCH should hit")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_cus_008" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_008" } catch (Exception ignore) {}
    }
}
