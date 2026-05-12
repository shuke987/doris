// JT-PARSE-023: key 无引号非法
suite("repro_jt_parse_023") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('{a:1}')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-023: unquoted key should throw")
}
