// JT-QUERY-030: json_search 'one' match
suite("repro_jt_query_030") {
    def r = sql "SELECT json_search(CAST('{\"a\":\"hi\"}' AS JSONB), 'one', 'hi')"
    String v = r[0][0].toString()
    assertTrue(v.contains("a"), "JT-QUERY-030; observed=${r}")
}
