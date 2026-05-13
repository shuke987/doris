// IIA-CUS-013: tokenizer=pinyin 中文转拼音
suite("repro_iia_cus_013") {
    sql "DROP TABLE IF EXISTS t_iia_cus_013"
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_013"
    try {
        sql """CREATE INVERTED INDEX ANALYZER IF NOT EXISTS iia_cus_013 PROPERTIES('tokenizer'='pinyin')"""
        Thread.sleep(10000)  // wait FE->BE policy sync
        sql """
            CREATE TABLE t_iia_cus_013 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='iia_cus_013'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_cus_013 VALUES (1,'北京')"
        // pinyin tokenizer 应使 'bei' 或 'jing' 命中
        def r1 = sql("SELECT count(*) FROM t_iia_cus_013 WHERE c MATCH 'bei'")
        def r2 = sql("SELECT count(*) FROM t_iia_cus_013 WHERE c MATCH 'jing'")
        // 不强制单字段，但至少一个拼音命中
        assertTrue(r1[0][0] >= 1L || r2[0][0] >= 1L,
                   "pinyin tokenizer: MATCH 'bei' or 'jing' should hit '北京'; r1=${r1[0][0]} r2=${r2[0][0]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_cus_013" } catch (Exception ignore) {}
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_013" } catch (Exception ignore) {}
    }
}
