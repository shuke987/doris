// JT-QUERY-032: json_search 'all' multiple
suite("repro_jt_query_032") {
    def r = sql "SELECT json_search(CAST('{\"a\":\"hi\",\"b\":\"hi\"}' AS JSONB), 'all', 'hi')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.contains("a") || v.contains("b"), "JT-QUERY-032; observed=${r}")
}
