// IIA-PRS-003: parser=unicode 等同 standard
suite("repro_iia_prs_003") {
    def r_std = sql """SELECT tokenize('Hello-World 中文', '"parser"="standard"')"""
    def r_uni = sql """SELECT tokenize('Hello-World 中文', '"parser"="unicode"')"""
    assertEquals(r_std[0][0].toString(), r_uni[0][0].toString(),
                 "parser=unicode should be alias for parser=standard")
}
