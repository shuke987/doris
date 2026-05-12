// JT-QUERY-020: json_keys top-level
suite("repro_jt_query_020") {
    def r = sql "SELECT json_keys(CAST('{\"a\":1,\"b\":2}' AS JSONB))"
    String v = r[0][0].toString()
    assertTrue(v.contains("a") && v.contains("b"), "JT-QUERY-020; observed=${r}")
}
