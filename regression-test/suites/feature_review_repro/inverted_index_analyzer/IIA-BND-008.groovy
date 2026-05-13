// IIA-BND-008: 长 token 截断 LUCENE_MAX_WORD_LEN=255 (实测确认)
suite("repro_iia_bnd_008") {
    def r = sql """SELECT tokenize(REPEAT('a', 300), '"parser"="english"')"""
    String s = r[0][0].toString()
    // 300 char 'a' → 应被切为 255 + 45 两个 token (LUCENE_MAX_WORD_LEN = 255)
    int n_tokens = (s =~ /"token":/).count
    assertTrue(n_tokens >= 2,
               "300-char token should be split (LUCENE_MAX_WORD_LEN=255); got ${n_tokens} tokens; s=${s}")
    // 第一个 token 应是 255 'a'
    String token_255 = 'a' * 255
    assertTrue(s.contains("\"token\": \"${token_255}\""),
               "first token should be 255 'a' chars; got=${s}")
}
