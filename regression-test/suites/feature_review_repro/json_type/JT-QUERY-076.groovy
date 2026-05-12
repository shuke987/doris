// JT-QUERY-076: contains 类型不匹配 lhs=array rhs=object
suite("repro_jt_query_076") {
    def r = sql """SELECT json_contains(CAST('[1,2]' AS JSONB), CAST('{"a":1}' AS JSONB))"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-076; observed=${r}")
}
