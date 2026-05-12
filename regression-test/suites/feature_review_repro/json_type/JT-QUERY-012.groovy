// JT-QUERY-012: json_contains NULL
suite("repro_jt_query_012") {
    def r = sql "SELECT json_contains(NULL, CAST('2' AS JSONB))"
    assertEquals(null, r[0][0], "JT-QUERY-012; observed=${r}")
}
