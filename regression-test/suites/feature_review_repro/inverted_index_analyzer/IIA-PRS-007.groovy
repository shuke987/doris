// IIA-PRS-007: parser=icu 多语言 Unicode word break
suite("repro_iia_prs_007") {
    def r1 = sql """SELECT tokenize('Hello 世界 안녕', '"parser"="icu"')"""
    String s1 = r1[0][0].toString()
    assertTrue(s1.contains('"token": "hello"') || s1.contains('"token": "Hello"'),
               "icu should produce English token; got=${s1}")
    // ICU 应处理中文 + 韩文
    assertTrue(s1.contains('世') || s1.contains('世界'),
               "icu should handle Chinese; got=${s1}")
    assertTrue(s1.contains('안') || s1.contains('안녕'),
               "icu should handle Korean; got=${s1}")
}
