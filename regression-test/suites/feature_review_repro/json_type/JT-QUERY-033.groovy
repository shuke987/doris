// JT-QUERY-033: json_search NULL jsonb
suite("repro_jt_query_033") {
    def r = sql "SELECT json_search(NULL, 'one', 'hi')"
    assertEquals(null, r[0][0], "JT-QUERY-033; observed=${r}")
}
