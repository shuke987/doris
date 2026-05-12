// JT-PARSE-043: -Infinity 非法
suite("repro_jt_parse_043") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('-Infinity')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-043: -Infinity should throw")
}
