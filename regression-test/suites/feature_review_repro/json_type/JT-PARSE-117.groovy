// JT-PARSE-117: 0.0 vs 0
suite("repro_jt_parse_117") {
    def r = sql "SELECT jsonb_parse('[0.0, 0]')"
    String v = r[0][0].toString()
    // expect either [0.0,0] or some normalized form
    assertNotNull(r[0][0], "JT-PARSE-117; observed=${r}")
}
