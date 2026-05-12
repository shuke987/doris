// JT-PARSE-018: 仅空格 应拒绝
suite("repro_jt_parse_018") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('   ')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-018: whitespace-only should throw")
}
