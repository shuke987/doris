// JT-QUERY-031: json_search no match
suite("repro_jt_query_031") {
    def r = sql "SELECT json_search(CAST('{\"a\":\"hi\"}' AS JSONB), 'one', 'nope')"
    assertEquals(null, r[0][0], "JT-QUERY-031; observed=${r}")
}
