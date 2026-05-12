// JT-QUERY-026: contains 大小写 string
suite("repro_jt_query_026") {
    def r = sql """SELECT json_contains(CAST('"abc"' AS JSONB), CAST('"ABC"' AS JSONB))"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-026; observed=${r}")
}
