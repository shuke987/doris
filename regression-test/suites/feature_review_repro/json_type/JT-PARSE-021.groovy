// JT-PARSE-021: 多余逗号 应拒绝
suite("repro_jt_parse_021") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('{\"a\":1,}')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-021: trailing comma should throw")
}
