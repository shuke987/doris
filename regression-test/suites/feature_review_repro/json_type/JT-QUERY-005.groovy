// JT-QUERY-005: json_length empty object
suite("repro_jt_query_005") {
    def r = sql "SELECT json_length(CAST('{}' AS JSONB))"
    assertEquals("0", r[0][0].toString(), "JT-QUERY-005; observed=${r}")
}
