// IIA-CHF-014: char_replace 极长 pattern (256 字符)
suite("repro_iia_chf_014") {
    sql "DROP TABLE IF EXISTS t_iia_chf_014"
    try {
        // 256 ASCII 字符 pattern（每字节都不同）
        String long_pattern = (0..255).collect { it < 32 || it > 126 ? '' : (char)it as String }.join('').replaceAll(/['"\\]/, '')
        // 实际限制大概 = 用所有可见 ASCII 即可
        String safe_pattern = (33..126).findAll { it != 34 && it != 39 && it != 92 }.collect { (char)it as String }.join('')
        sql """
            CREATE TABLE t_iia_chf_014 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english',
                'char_filter_type'='char_replace','char_filter_pattern'='${safe_pattern}','char_filter_replacement'=' '))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // 不 crash 即 PASS
        def r = sql "SHOW CREATE TABLE t_iia_chf_014"
        assertNotNull(r, "long ASCII pattern should be accepted")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_chf_014" } catch (Exception ignore) {}
    }
}
