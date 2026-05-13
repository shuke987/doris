// IIA-MOD-003: parser=ik + parser_mode=ik_smart
suite("repro_iia_mod_003") {
    def r = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_smart"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token":'),
               "ik smart should produce tokens; got=${s}")
}
