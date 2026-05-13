// IIA-BND-004: emoji 在 english parser 中行为
suite("repro_iia_bnd_004") {
    def r = sql """SELECT tokenize('hello 👋 world', '"parser"="english"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token": "hello"'),
               "english should produce 'hello' separating from emoji; got=${s}")
    assertTrue(s.contains('"token": "world"'),
               "english should produce 'world' separating from emoji; got=${s}")
    // emoji 不应产 token（不在 alnum 范围）
    assertFalse(s.contains('"token": "👋"'),
                "english should NOT tokenize emoji; got=${s}")
}
