// JT-QUERY-042: json_unquote NULL
suite("repro_jt_query_042") {
    def r = sql "SELECT json_unquote(NULL)"
    assertEquals(null, r[0][0], "JT-QUERY-042; observed=${r}")
}
