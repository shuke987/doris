// JT-QUERY-017: contains 类型不匹配
suite("repro_jt_query_017") {
    def r = sql """SELECT json_contains(CAST('1' AS JSONB), CAST('"1"' AS JSONB), '\$')"""
    assertTrue(['0','false'].contains(r[0][0]?.toString()), "JT-QUERY-017; observed=${r}")
}
