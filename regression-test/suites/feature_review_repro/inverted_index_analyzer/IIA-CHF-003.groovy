// IIA-CHF-003 (SEV-1 DORIS-25637 #N1b): char_replace replacement='' 应不注入 NUL 字节
// Spec correct (建议)：empty replacement 应 (a) 删除匹配字符 → tokens ["abc"]，或 (b) FE 拒绝（length=1 + non-empty）
// 当前 4.1: 注入 \0 字节 + english split → tokens ["a","b","c"] → FAIL = bug signal
suite("repro_iia_chf_003") {
    def r1 = sql """SELECT tokenize('a.b.c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"=""')"""
    String s1 = r1[0][0].toString()
    // 期望（修复后）：empty replacement = delete → 单 token "abc"
    assertTrue(s1.contains('"token": "abc"'),
        "Empty char_filter_replacement MUST be either deleted (→ 'abc') or rejected by FE (DORIS-25637). Current: \\0 byte injected, tokens=['a','b','c']; result=${s1}")

    // parser=none should_analyzer=false 时 char_filter 不应用 (这部分 correct)
    def r2 = sql """SELECT tokenize('a.b.c', '"parser"="none","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"=""')"""
    String s2 = r2[0][0].toString()
    assertTrue(s2.contains('"token": "a.b.c"'),
        "parser=none should skip char_filter (correct); result=${s2}")
}
