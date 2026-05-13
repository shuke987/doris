// IIA-PRS-019: 不指定 parser → 默认整字段（与 parser=none 同）
suite("repro_iia_prs_019") {
    def r = sql """SELECT tokenize('hello world', '')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token": "hello world"'),
               "no parser should default to full-field token; got=${s}")
}
