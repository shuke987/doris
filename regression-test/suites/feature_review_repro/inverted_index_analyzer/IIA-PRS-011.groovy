// IIA-PRS-011: parser=ik max_word mode (相比 smart 更细)
suite("repro_iia_prs_011") {
    def r_smart = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_smart"')"""
    def r_max = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_max_word"')"""
    String s_smart = r_smart[0][0].toString()
    String s_max = r_max[0][0].toString()
    // max_word 应产更多 token
    int n_smart = (s_smart =~ /"token":/).count
    int n_max = (s_max =~ /"token":/).count
    assertTrue(n_max >= n_smart,
               "ik_max_word should produce >= tokens than ik_smart; smart=${n_smart} max=${n_max}")
}
