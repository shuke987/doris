// JT-PARSE-022: 单引号非法
suite("repro_jt_parse_022") {
    boolean threw = false
    try { sql "SELECT jsonb_parse(\"{'a':1}\")" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-022: single-quoted JSON should throw")
}
