// IIA-PRS-006: parser=chinese fine_grained 多 token
suite("repro_iia_prs_006") {
    def r1 = sql """SELECT tokenize('我爱北京天安门', '"parser"="chinese","parser_mode"="fine_grained"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "北京"'),
               "chinese fine should still produce 北京; got=${s1}")
    assertTrue(s1.contains('"token": "天安门"'),
               "chinese fine should still produce 天安门; got=${s1}")
    // fine 应额外产 "天安"
    assertTrue(s1.contains('"token": "天安"'),
               "chinese fine_grained should ALSO produce intermediate '天安'; got=${s1}")
}
