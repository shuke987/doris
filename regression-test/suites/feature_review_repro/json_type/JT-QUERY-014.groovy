// JT-QUERY-014: json_contains scalar (top-level)
suite("repro_jt_query_014") {
    def r = sql "SELECT json_contains(CAST('5' AS JSONB), CAST('5' AS JSONB))"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-QUERY-014; observed=${r}")
}
