// JT-QUERY-011: json_contains missing
suite("repro_jt_query_011") {
    def r = sql "SELECT json_contains(CAST('[1,2,3]' AS JSONB), CAST('5' AS JSONB))"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "0" || v == "false", "JT-QUERY-011; observed=${r}")
}
