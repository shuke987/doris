// JT-QUERY-007: length 字符串值
suite("repro_jt_query_007") {
    def r = sql """SELECT json_length(CAST('"hello"' AS JSONB))"""
    assertEquals('1', r[0][0]?.toString(), "JT-QUERY-007; observed=${r}")
}
