// JT-QUERY-066: json_unquote 正向
suite("repro_jt_query_066") {
    def r = sql """SELECT json_unquote('"hi"')"""
    assertEquals('hi', r[0][0]?.toString(), "JT-QUERY-066; observed=${r}")
}
