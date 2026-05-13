// IIA-BND-017: 全标点字符
suite("repro_iia_bnd_017") {
    def r = sql """SELECT tokenize('!!!@@@###', '"parser"="english"')"""
    String s = r[0][0].toString()
    // english 把标点视为分隔，0 token
    assertFalse(s.contains('"token": "!"') || s.contains('"token": "@"'),
                "english should NOT tokenize punctuation; got=${s}")
}
