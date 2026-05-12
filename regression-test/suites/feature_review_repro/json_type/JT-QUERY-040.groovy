// JT-QUERY-040: json_quote
suite("repro_jt_query_040") {
    def r = sql "SELECT json_quote('hello')"
    assertEquals("\"hello\"", r[0][0].toString(), "JT-QUERY-040; observed=${r}")
}
