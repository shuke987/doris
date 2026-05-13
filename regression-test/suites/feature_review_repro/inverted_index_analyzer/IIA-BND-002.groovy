// IIA-BND-002: 全空白字符串
suite("repro_iia_bnd_002") {
    def r = sql """SELECT tokenize('   ', '"parser"="english"')"""
    String s = r[0][0].toString()
    // 全空白 → 无 alnum token
    assertFalse(s.contains('"token": "'),
                "english on all-whitespace should produce no tokens; got=${s}")
}
