// JT-PARSE-050: int16 边界 32767
suite("repro_jt_parse_050") {
    def r = sql """SELECT json_type(jsonb_parse('32767'), '\$')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('smallint'), "observed=${r}")
}
