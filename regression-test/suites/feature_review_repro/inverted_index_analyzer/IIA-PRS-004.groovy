// IIA-PRS-004: parser=english 隐式 lowercase
suite("repro_iia_prs_004") {
    def r1 = sql """SELECT tokenize('Hello WORLD', '"parser"="english"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "hello"'),
               "parser=english should lowercase by default; got=${s1}")
    assertTrue(s1.contains('"token": "world"'),
               "parser=english should lowercase WORLD→world; got=${s1}")
    assertFalse(s1.contains('"token": "Hello"'),
                "parser=english default should NOT preserve case; got=${s1}")
}
