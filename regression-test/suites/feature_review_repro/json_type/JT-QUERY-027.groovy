// JT-QUERY-027: contains 中文/emoji
suite("repro_jt_query_027") {
    def r = sql """SELECT json_contains(CAST('"中文"' AS JSONB), CAST('"中文"' AS JSONB))"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-QUERY-027; observed=${r}")
}
