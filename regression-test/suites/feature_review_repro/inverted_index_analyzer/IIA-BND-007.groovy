// IIA-BND-007: 控制字符 NUL/\\r/\\n 不 crash 且作为分隔符
suite("repro_iia_bnd_007") {
    // \0 + \r + \n
    def r = sql """SELECT tokenize('a\\0b\\rc\\nd', '"parser"="english"')"""
    String s = r[0][0].toString()
    // 控制字符应作为分隔符
    assertTrue(s.contains('"token": "a"'),
               "control char should split: 'a' token expected; got=${s}")
    assertTrue(s.contains('"token": "b"'),
               "control char should split: 'b' token expected; got=${s}")
    assertTrue(s.contains('"token": "c"'),
               "control char should split: 'c' token expected; got=${s}")
    assertTrue(s.contains('"token": "d"'),
               "control char should split: 'd' token expected; got=${s}")
}
