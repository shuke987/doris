// JT-PARSE-116: -0 vs 0
suite("repro_jt_parse_116") {
    def r = sql "SELECT jsonb_parse('[-0, 0]')"
    String v = r[0][0].toString()
    assertTrue(v.contains("0"), "JT-PARSE-116; observed=${r}")
}
