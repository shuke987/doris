// JT-QUERY-016: contains scalar
suite("repro_jt_query_016") {
    def r = sql """SELECT json_contains(CAST('1' AS JSONB), CAST('1' AS JSONB), '\$')"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-016; observed=${r}")
}
