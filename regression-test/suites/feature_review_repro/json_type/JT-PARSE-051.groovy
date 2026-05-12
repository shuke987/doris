// JT-PARSE-051: int16 边界 32768
suite("repro_jt_parse_051") {
    def r = sql """SELECT json_type(jsonb_parse('32768'), '\$')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('int'), "observed=${r}")
}
