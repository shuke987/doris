// JT-QUERY-002: json_length array
suite("repro_jt_query_002") {
    def r = sql "SELECT json_length(CAST('[1,2,3]' AS JSONB))"
    assertEquals("3", r[0][0].toString(), "JT-QUERY-002; observed=${r}")
}
