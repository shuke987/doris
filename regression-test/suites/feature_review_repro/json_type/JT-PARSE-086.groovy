// JT-PARSE-086: nullable 契约
suite("repro_jt_parse_086") {
    def r = sql """SELECT jsonb_parse('{"a":1}')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a"'), "JT-PARSE-086; observed=${r}")
}
