// JT-QUERY-052: json_valid NULL
suite("repro_jt_query_052") {
    def r = sql "SELECT json_valid(NULL)"
    assertEquals(null, r[0][0], "JT-QUERY-052; observed=${r}")
}
