// IIA-CHF-004 (SEV-1 #N1a): char_replace replacement='xyz' (多字符) 只用首字节
suite("repro_iia_chf_004") {
    // pattern='.' replacement='xyz' → 期望 "a.b.c" → "axyzbxyzc"
    // 实际 BE _replacement[0] 只取 'x' → "axbxc"
    def r1 = sql """SELECT tokenize('a.b.c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"="xyz"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "axbxc"'),
               "IIA-CHF-004: SEV-1 #N1a reproduce — multi-char replacement only uses first byte; result=${s1}")
    assertFalse(s1.contains('"token": "axyzbxyzc"'),
                "IIA-CHF-004: SEV-1 #N1a — full replacement NOT used (bug); result=${s1}")

    // 进一步：replacement='Q W' (含空格) → 't' → 'Q'（首字符，lowercase 后 'q'）
    def r2 = sql """SELECT tokenize('test', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"="t","char_filter_replacement"="Q W"')"""
    String s2 = r2[0][0].toString()
    assertTrue(s2.contains('"token": "qesq"'),
               "IIA-CHF-004: replacement='Q W' only first byte 'Q' used → lowercased 'q'; result=${s2}")
}
