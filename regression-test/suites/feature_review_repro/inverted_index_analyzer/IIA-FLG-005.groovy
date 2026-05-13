// IIA-FLG-005: lower_case=false 显式 (大小写敏感)
suite("repro_iia_flg_005") {
    def r = sql """SELECT tokenize('Hello WORLD', '"parser"="english","lower_case"="false"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token": "Hello"'),
               "lower_case=false should preserve 'Hello'; got=${s}")
    assertTrue(s.contains('"token": "WORLD"'),
               "lower_case=false should preserve 'WORLD'; got=${s}")
    assertFalse(s.contains('"token": "hello"'),
                "lower_case=false should NOT lowercase; got=${s}")
}
