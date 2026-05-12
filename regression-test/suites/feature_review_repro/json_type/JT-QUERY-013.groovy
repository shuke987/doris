// JT-QUERY-013: json_contains nested
suite("repro_jt_query_013") {
    def r = sql "SELECT json_contains(CAST('{\"a\":{\"x\":1}}' AS JSONB), CAST('{\"x\":1}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-QUERY-013; observed=${r}")
}
