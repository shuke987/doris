// IIA-CHF-004 (SEV-1 DORIS-25637 #N1a): char_replace 多字符 replacement 必须全替换
// Spec correct (建议)：replacement='xyz' 应 (a) 全替换 → "a.b.c" → "axyzbxyzc"，或 (b) FE 拒绝（length>1）
// 当前 4.1: BE `_replacement[0]` 只取首字节 → "axbxc" → FAIL = bug signal
suite("repro_iia_chf_004") {
    def r1 = sql """SELECT tokenize('a.b.c', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"=".","char_filter_replacement"="xyz"')"""
    String s1 = r1[0][0].toString()
    // 期望（修复后）：multi-char replacement 全用 → "axyzbxyzc" 单 token
    assertTrue(s1.contains('"token": "axyzbxyzc"'),
        "Multi-char char_filter_replacement MUST fully replace or FE reject (DORIS-25637). Current: only first byte 'x' used, got 'axbxc' losing 'yz'; result=${s1}")

    // replacement='Q W' (含空格 + lowercase) → 期望全替换后 english parser 处理
    // 'test' 中 't' → 'Q W' → "Q West" 等价；english lowercase → "q west" 多 token
    // 当前 4.1: 只首字节 'Q' (lowercase 'q') 替换 → "qesq" 单 token (bug signal)
    def r2 = sql """SELECT tokenize('test', '"parser"="english","char_filter_type"="char_replace","char_filter_pattern"="t","char_filter_replacement"="Q W"')"""
    String s2 = r2[0][0].toString()
    // 期望（修复后）：'test' 中所有 't' 替换为 "Q W" → "Q West" 或类似 → lowercased + split
    // 至少应包含 "q" 和 "w" tokens（lowercase 后）
    assertTrue((s2.contains('"token": "q"') && s2.contains('"token": "w"')) ||
               s2.contains('"token": "q west"') ||
               !s2.contains('"token": "qesq"'),
        "Multi-char replacement with space MUST fully replace and re-tokenize correctly; current: 'qesq' (only first 'Q' used, no W); result=${s2}")
}
