// JT-QUERY-041: json_unquote
suite("repro_jt_query_041") {
    def r = sql "SELECT json_unquote('\"hello\"')"
    assertEquals("hello", r[0][0].toString(), "JT-QUERY-041; observed=${r}")
}
