// JT-QUERY-006: json_length empty array
suite("repro_jt_query_006") {
    def r = sql "SELECT json_length(CAST('[]' AS JSONB))"
    assertEquals("0", r[0][0].toString(), "JT-QUERY-006; observed=${r}")
}
