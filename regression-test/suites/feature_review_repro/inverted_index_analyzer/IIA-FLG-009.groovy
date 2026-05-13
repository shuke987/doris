// IIA-FLG-009 (partial SEV): support_phrase=false 索引 + MATCH_PHRASE 查询 BE 报错
suite("repro_iia_flg_009") {
    sql "DROP TABLE IF EXISTS t_iia_flg_009"
    try {
        sql """
            CREATE TABLE t_iia_flg_009 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english', 'support_phrase'='false'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_flg_009 VALUES (1,'doris is fast'),(2,'hello world')"
        // MATCH 不需 phrase 信息 → 可工作
        def r1 = sql "SELECT count(*) FROM t_iia_flg_009 WHERE c MATCH 'doris'"
        assertEquals(1L, r1[0][0], "MATCH 单 token 应工作")

        // MATCH_PHRASE 需 phrase 信息 → 应报错（BE 层 detect）
        boolean threw = false
        try {
            sql "SELECT * FROM t_iia_flg_009 WHERE c MATCH_PHRASE 'doris is'"
        } catch (Exception e) {
            threw = true
            assertTrue(e.getMessage().contains('support_phrase'),
                       "BE error message should mention support_phrase; got=${e.getMessage()}")
        }
        assertTrue(threw, "MATCH_PHRASE on support_phrase=false index should fail")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_009" } catch (Exception ignore) {}
    }
}
