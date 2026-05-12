// JT-QUERY-065: json_quote 正向
suite("repro_jt_query_065") {
    def r = sql """SELECT json_quote('hi')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"hi"'), "JT-QUERY-065; observed=${r}")
}
