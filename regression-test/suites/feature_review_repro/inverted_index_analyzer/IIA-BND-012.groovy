// IIA-BND-012: 全中文 1000 字（性能/正确性 baseline）
suite("repro_iia_bnd_012") {
    String long_zh = ('你好世界' * 250)  // 1000 个中文字符
    def r = sql """SELECT tokenize('${long_zh}', '"parser"="chinese","parser_mode"="coarse_grained"')"""
    String s = r[0][0].toString()
    // 不 crash + 有 token
    assertTrue(s.contains('"token":'),
               "chinese should tokenize 1000-char document; got token count: ${(s =~ /\"token\":/).count}")
}
