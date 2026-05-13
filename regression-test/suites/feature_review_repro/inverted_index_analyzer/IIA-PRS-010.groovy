// IIA-PRS-010: parser=ik smart mode
suite("repro_iia_prs_010") {
    def r1 = sql """SELECT tokenize('全文检索引擎', '"parser"="ik","parser_mode"="ik_smart"')"""
    String s1 = r1[0][0].toString()
    // ik smart 应产长 token（如 '全文' '检索' '引擎'）
    assertTrue(s1.contains('"token"'),
               "ik smart should produce tokens; got=${s1}")
    // 不应只产单字
    int single_char_count = 0
    def matcher = s1 =~ /"token": "[^"]{3}"/   // ≥3 chars = multi-byte token
    if (matcher) single_char_count = matcher.count
    // ik_smart 应至少有一个多字节 token
    assertTrue(s1.contains('"token": "全文"') || s1.contains('"token": "检索"') || s1.contains('"token": "引擎"'),
               "ik smart should produce coarse tokens; got=${s1}")
}
