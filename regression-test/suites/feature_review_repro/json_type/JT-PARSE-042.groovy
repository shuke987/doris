// JT-PARSE-042: Infinity literal 非法
suite("repro_jt_parse_042") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('Infinity')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-042: Infinity literal should throw")
}
