// JT-PARSE-025: trailing 字符 应拒绝
suite("repro_jt_parse_025") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('{\"a\":1}garbage')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-025: trailing garbage should throw")
}
