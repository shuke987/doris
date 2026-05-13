// IIA-PRS-022: parser=icu 数字字母混合
suite("repro_iia_prs_022") {
    def r = sql """SELECT tokenize('i18n version2.0', '"parser"="icu"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token":'),
               "icu should produce tokens for mixed alnum; got=${s}")
}
