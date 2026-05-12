// JT-PARSE-020: 非法 JSON 缺右括号
suite("repro_jt_parse_020") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('{a:1')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-020: malformed JSON should throw")
}
