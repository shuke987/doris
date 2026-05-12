// JT-PARSE-019: 仅 tab/换行 (only whitespace)
suite("repro_jt_parse_019") {
    boolean threw = false
    try { sql "SELECT jsonb_parse('\t\n')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-019: whitespace-only should throw")
}
