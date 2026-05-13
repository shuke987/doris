// IIA-PRS-008: parser=basic alnum + Chinese single char
suite("repro_iia_prs_008") {
    def r1 = sql """SELECT tokenize('abc中文', '"parser"="basic"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "abc"'),
               "basic should produce 'abc' single token; got=${s1}")
    assertTrue(s1.contains('"token": "中"'),
               "basic should split chinese into single chars; got=${s1}")
    assertTrue(s1.contains('"token": "文"'),
               "basic should split chinese into single chars; got=${s1}")
}
