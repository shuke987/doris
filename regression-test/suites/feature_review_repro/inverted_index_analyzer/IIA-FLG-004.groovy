// IIA-FLG-004: lower_case 默认行为（parser=english 隐式 lower）
suite("repro_iia_flg_004") {
    def r_default = sql """SELECT tokenize('ABCdef', '"parser"="english"')"""
    def r_explicit = sql """SELECT tokenize('ABCdef', '"parser"="english","lower_case"="true"')"""
    // 两者应一致 都 lowercase
    String s_default = r_default[0][0].toString()
    String s_explicit = r_explicit[0][0].toString()
    assertTrue(s_default.contains('"token": "abcdef"'),
               "default lower_case should be true for parser=english; got=${s_default}")
    assertTrue(s_explicit.contains('"token": "abcdef"'),
               "explicit lower_case=true should match default; got=${s_explicit}")
    assertEquals(s_default, s_explicit,
                 "default and explicit lower_case=true should be identical")
}
