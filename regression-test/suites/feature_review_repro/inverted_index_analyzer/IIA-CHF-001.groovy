// IIA-CHF-001: char_replace 单 ASCII 字符（正向 baseline）
suite("repro_iia_chf_001") {
    def r = sql """SELECT tokenize('a.b_c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"="._","char_filter_replacement"=" "')"""
    String s = r[0][0].toString()
    // 期望 . 和 _ 替换为空格 → 'a b c' → ['a','b','c']
    assertTrue(s.contains('"token": "a"'),
               "char_replace should produce 'a'; got=${s}")
    assertTrue(s.contains('"token": "b"'),
               "char_replace should produce 'b'; got=${s}")
    assertTrue(s.contains('"token": "c"'),
               "char_replace should produce 'c'; got=${s}")
}
