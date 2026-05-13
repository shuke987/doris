// IIA-FLG-001: stopwords='none' (显式关闭) — 与默认行为对比
suite("repro_iia_flg_001") {
    def r1 = sql """SELECT tokenize('the quick brown fox', '"parser"="english","stopwords"="none"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "the"'),
               "stopwords=none should preserve 'the'; got=${s1}")
    assertTrue(s1.contains('"token": "quick"'),
               "stopwords=none should preserve 'quick'; got=${s1}")
    assertTrue(s1.contains('"token": "brown"'),
               "stopwords=none should preserve 'brown'; got=${s1}")
    assertTrue(s1.contains('"token": "fox"'),
               "stopwords=none should preserve 'fox'; got=${s1}")
}
