// IIA-CHF-003 (SEV-1 #N1b): char_replace replacement='' (empty) 注入 NUL 字节
// 验证：通过 tokenize() 直接观察 BE 分词器输出
suite("repro_iia_chf_003") {
    // parser=english + pattern='.' + replacement='' :  期望"删除 ." 但实际注入 \0 字节
    def r1 = sql """SELECT tokenize('a.b.c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"=""')"""
    String s1 = r1[0][0].toString()
    // 期望 spec 应为 ["abc"]（删除 .）；实际 \0 注入 + english split on non-alnum → ["a","b","c"]
    boolean has_three = s1.contains('"token": "a"') && s1.contains('"token": "b"') && s1.contains('"token": "c"')
    assertTrue(has_three, "IIA-CHF-003: SEV-1 #N1b reproduce — empty replacement injects NUL byte; result=${s1}")

    // 同样配置 parser=none → char_filter 不应用（should_analyzer=false 时跳过）
    def r2 = sql """SELECT tokenize('a.b.c', '"parser"="none","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"=""')"""
    String s2 = r2[0][0].toString()
    // parser=none 不分词 + char_filter 不应用 → 原文整字段
    assertTrue(s2.contains('"token": "a.b.c"'),
               "IIA-CHF-003: parser=none should skip char_filter; result=${s2}")
}
