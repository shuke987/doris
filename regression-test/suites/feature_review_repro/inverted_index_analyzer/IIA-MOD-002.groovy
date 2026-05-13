// IIA-MOD-002: chinese parser_mode=fine_grained 行为
suite("repro_iia_mod_002") {
    def r = sql """SELECT tokenize('北京大学', '"parser"="chinese","parser_mode"="fine_grained"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token":'), "fine_grained should produce at least one token; got=${s}")
    // fine 应较 coarse 产 ≥ token
    int n_fine = (s =~ /"token":/).count
    def r2 = sql """SELECT tokenize('北京大学', '"parser"="chinese","parser_mode"="coarse_grained"')"""
    int n_coarse = (r2[0][0].toString() =~ /"token":/).count
    assertTrue(n_fine >= n_coarse,
               "fine_grained tokens (${n_fine}) >= coarse_grained tokens (${n_coarse})")
}
