// IIA-FLG-007: support_phrase=true 默认 MATCH_PHRASE 可用
suite("repro_iia_flg_007") {
    sql "DROP TABLE IF EXISTS t_iia_flg_007"
    try {
        sql """
            CREATE TABLE t_iia_flg_007 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','support_phrase'='true'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_flg_007 VALUES (1,'apache doris fast')"
        def r = sql "SELECT count(*) FROM t_iia_flg_007 WHERE c MATCH_PHRASE 'apache doris'"
        assertEquals(1L, r[0][0], "support_phrase=true: MATCH_PHRASE works")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_007" } catch (Exception ignore) {}
    }
}
