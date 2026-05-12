// JT-QUERY-025: contains 路径不存在
suite("repro_jt_query_025") {
    def r = sql """SELECT json_contains(CAST('{"a":1}' AS JSONB), CAST('1' AS JSONB), '\$.nope')"""
    assertEquals(null, r[0][0], "JT-QUERY-025: expect NULL; observed=${r}")
}
