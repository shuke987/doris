// IIA-BND-006: RTL 阿拉伯/希伯来 parser=icu
suite("repro_iia_bnd_006") {
    def r = sql """SELECT tokenize('مرحبا بالعالم', '"parser"="icu"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token":'),
               "icu should tokenize Arabic; got=${s}")
}
