// JT-QUERY-072: `json_contains('[1,1,1]', '[1,1]')` lhs 含重复值
suite("repro_jt_query_072") {
    def r = sql """SELECT json_contains(CAST('[1,1,1]' AS JSONB), CAST('[1,1]' AS JSONB))"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-072; observed=${r}")
}
