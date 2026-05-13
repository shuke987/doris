// IIA-PRS-001: parser=none 整字段作为 1 token (baseline tokenize)
suite("repro_iia_prs_001") {
    def r1 = sql """SELECT tokenize('hello world', '"parser"="none"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "hello world"'),
               "parser=none should produce single full-field token; got=${s1}")

    // 空串
    def r2 = sql """SELECT tokenize('', '"parser"="none"')"""
    String s2 = r2[0][0].toString()
    assertTrue(s2.contains('"token": ""'),
               "parser=none on empty string should produce one empty token; got=${s2}")
}
