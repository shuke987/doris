// JT-QUERY-010: json_contains array
suite("repro_jt_query_010") {
    def r = sql "SELECT json_contains(CAST('[1,2,3]' AS JSONB), CAST('2' AS JSONB))"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-QUERY-010; observed=${r}")
}
