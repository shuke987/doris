// JT-QUERY-075: contains 类型不匹配 lhs=null rhs=anything
suite("repro_jt_query_075") {
    def r = sql """SELECT json_contains(CAST('null' AS JSONB), CAST('1' AS JSONB))"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-075; observed=${r}")
}
