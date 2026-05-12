// JT-QUERY-004: json_length NULL
suite("repro_jt_query_004") {
    def r = sql "SELECT json_length(NULL)"
    assertEquals(null, r[0][0], "JT-QUERY-004; observed=${r}")
}
