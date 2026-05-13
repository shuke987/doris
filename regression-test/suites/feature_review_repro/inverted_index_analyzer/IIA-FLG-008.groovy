// IIA-FLG-008: support_phrase=false + non-phrase MATCH 可用
suite("repro_iia_flg_008") {
    sql "DROP TABLE IF EXISTS t_iia_flg_008"
    try {
        sql """
            CREATE TABLE t_iia_flg_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','support_phrase'='false'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_flg_008 VALUES (1,'doris is fast')"
        // 普通 MATCH 应可用
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_flg_008 WHERE c MATCH 'doris'")[0][0],
                     "support_phrase=false + MATCH single token should work")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_flg_008 WHERE c MATCH_ANY 'doris foo'")[0][0],
                     "support_phrase=false + MATCH_ANY should work")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_flg_008 WHERE c MATCH_ALL 'doris is'")[0][0],
                     "support_phrase=false + MATCH_ALL should work")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_008" } catch (Exception ignore) {}
    }
}
