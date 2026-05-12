// JT-QUERY-074: contains 类型不匹配 lhs=object rhs=string
suite("repro_jt_query_074") {
    def r = sql """SELECT json_contains(CAST('{"a":1}' AS JSONB), CAST('"a"' AS JSONB))"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-074; observed=${r}")
}
