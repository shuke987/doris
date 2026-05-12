// JT-QUERY-008: length path 不存在
suite("repro_jt_query_008") {
    def r = sql """SELECT json_length(CAST('{"a":1}' AS JSONB), '\$.b')"""
    assertEquals(null, r[0][0], "JT-QUERY-008: expect NULL; observed=${r}")
}
