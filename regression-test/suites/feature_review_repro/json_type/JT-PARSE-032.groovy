// JT-PARSE-032: key 含反斜杠 escape
suite("repro_jt_parse_032") {
    def r = sql """SELECT jsonb_parse('{"a\\\\b":1}')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('1'), "observed=${r}")
}
