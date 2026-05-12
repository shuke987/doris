// JT-PARSE-111: 负整数
suite("repro_jt_parse_111") {
    def r = sql """SELECT jsonb_parse('[-1, -200, -70000]')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('-1'), "observed=${r}")
}
