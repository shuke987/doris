// JT-QUERY-001: json_length object
suite("repro_jt_query_001") {
    def r = sql "SELECT json_length(CAST('{\"a\":1,\"b\":2}' AS JSONB))"
    assertEquals("2", r[0][0].toString(), "JT-QUERY-001; observed=${r}")
}
