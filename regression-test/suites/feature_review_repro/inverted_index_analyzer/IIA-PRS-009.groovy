// IIA-PRS-009: parser=basic + extra_chars
// 实测 step52: BasicTokenizer extra_chars 仅 ASCII (< 128) 生效，多字节静默忽略
suite("repro_iia_prs_009") {
    // basic + 默认 extra_chars 空：'a@b_c' '_' '@' 都视为分隔符
    def r1 = sql """SELECT tokenize('a@b_c', '"parser"="basic"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "a"') && s1.contains('"token": "b"') && s1.contains('"token": "c"'),
               "basic should split on @ and _; got=${s1}")
}
