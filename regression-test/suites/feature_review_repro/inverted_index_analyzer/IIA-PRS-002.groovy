// IIA-PRS-002: parser=standard 分词
suite("repro_iia_prs_002") {
    def r1 = sql """SELECT tokenize('Hello-World 中文', '"parser"="standard"')"""
    String s1 = r1[0][0].toString()
    // standard 应切出 hello/world/中/文 (4 token，case sensitive 除非 lower_case)
    assertTrue(s1.contains('"token": "hello"') || s1.contains('"token": "Hello"'),
               "standard should produce 'hello' or 'Hello' token; got=${s1}")
    assertTrue(s1.contains('"token": "world"') || s1.contains('"token": "World"'),
               "standard should produce 'world' or 'World' token; got=${s1}")
    assertTrue(s1.contains('"token": "中"'),
               "standard should split chinese into single chars; got=${s1}")
    assertTrue(s1.contains('"token": "文"'),
               "standard should split chinese into single chars; got=${s1}")
}
