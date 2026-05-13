// IIA-MOD-004: parser=ik + parser_mode=ik_max_word produces more tokens than smart
suite("repro_iia_mod_004") {
    def smart = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_smart"')"""
    def maxw = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_max_word"')"""
    int n_smart = (smart[0][0].toString() =~ /"token":/).count
    int n_max = (maxw[0][0].toString() =~ /"token":/).count
    assertTrue(n_max >= n_smart,
               "ik_max_word should produce >= tokens than ik_smart; smart=${n_smart} max=${n_max}")
}
