// IIA-BND-009: 全 emoji 文档
suite("repro_iia_bnd_009") {
    def r = sql """SELECT tokenize('😀😀😀', '"parser"="english"')"""
    String s = r[0][0].toString()
    // english 不识别 emoji，无 alnum token
    assertFalse(s.contains('"token": "😀"'),
                "english should NOT tokenize emoji; got=${s}")
}
