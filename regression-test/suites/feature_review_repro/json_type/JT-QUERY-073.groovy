// JT-QUERY-073: contains 类型不匹配 lhs=int rhs=array
suite("repro_jt_query_073") {
    def r = sql """SELECT json_contains(CAST('1' AS JSONB), CAST('[1]' AS JSONB))"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-073; observed=${r}")
}
