// JT-PARSE-041: NaN literal 非法
suite("repro_jt_parse_041") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('NaN')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-041: NaN literal should throw")
}
