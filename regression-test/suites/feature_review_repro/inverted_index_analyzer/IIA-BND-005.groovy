// IIA-BND-005: 零宽字符 ZWSP (U+200B) 在 parser=english 中行为
suite("repro_iia_bnd_005") {
    // 'a​b' (a + ZWSP + b)
    def r = sql """SELECT tokenize('a​b', '"parser"="english"')"""
    String s = r[0][0].toString()
    // ZWSP 非 alnum，english 应作为分隔
    assertTrue(s.contains('"token":'), "should not crash on ZWSP; got=${s}")
}
