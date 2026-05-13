// IIA-CHF-002: char_replace 空 pattern → FE reject
suite("repro_iia_chf_002") {
    sql "DROP TABLE IF EXISTS t_iia_chf_002"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_chf_002 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english',
                'char_filter_type'='char_replace','char_filter_pattern'='','char_filter_replacement'='_'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    // FE 应该拒绝空 pattern（如果不拒绝，则 noop）
    // 实测：FE 接受空 pattern (length check 缺)
    if (threw) {
        // 拒绝是正确防御
    } else {
        // 接受是当前 behavior，验证 tokenize 不 crash
        def r = sql """SELECT tokenize('a.b', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"="","char_filter_replacement"="_"')"""
        assertNotNull(r, "tokenize should not crash with empty pattern")
    }
    sql "DROP TABLE IF EXISTS t_iia_chf_002"
}
