// IIA-PRS-005: parser=chinese coarse_grained 默认行为
suite("repro_iia_prs_005") {
    def r1 = sql """SELECT tokenize('我爱北京天安门', '"parser"="chinese","parser_mode"="coarse_grained"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "北京"'),
               "chinese coarse should produce 北京; got=${s1}")
    assertTrue(s1.contains('"token": "天安门"'),
               "chinese coarse should produce 天安门 (long token preferred); got=${s1}")
    // coarse 不应产 "天安" 子段
    assertFalse(s1.contains('"token": "天安"') && !s1.contains('"token": "天安门"'),
                "chinese coarse should NOT produce intermediate '天安'; got=${s1}")
}
