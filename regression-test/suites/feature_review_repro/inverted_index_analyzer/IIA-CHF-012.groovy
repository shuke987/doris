// IIA-CHF-012: char_filter_type='char_replace' 缺 replacement → FE ACCEPT
// Doc (overview.md):"char_filter_replacement: replacement character array, **optional, defaults to a space character**"
// 即缺 replacement 是合法的，BE 默认替换为单个空格
suite("repro_iia_chf_012") {
    sql "DROP TABLE IF EXISTS t_iia_chf_012"
    try {
        sql """
            CREATE TABLE t_iia_chf_012 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english',
                'char_filter_type'='char_replace','char_filter_pattern'='.'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def r = sql "SHOW CREATE TABLE t_iia_chf_012"
        assertNotNull(r, "missing char_filter_replacement should be accepted (default space)")
        // 验证默认空格行为：tokenize 'a.b.c' 应等同于 replacement=' '
        def t = sql """SELECT tokenize('a.b.c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"="."')"""
        String tokens = t[0][0].toString()
        assertTrue(tokens.contains('"token": "a"') && tokens.contains('"token": "b"') && tokens.contains('"token": "c"'),
                   "default replacement (space) should split a.b.c → [a,b,c]; got=${tokens}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_chf_012" } catch (Exception ignore) {}
    }
}
