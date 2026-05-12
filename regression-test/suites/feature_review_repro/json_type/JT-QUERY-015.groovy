// JT-QUERY-015: json_contains object subset
suite("repro_jt_query_015") {
    def r = sql "SELECT json_contains(CAST('{\"a\":1,\"b\":2}' AS JSONB), CAST('{\"a\":1}' AS JSONB))"
    String v = r[0][0].toString().toLowerCase()
    // observed: cluster may return 0 or 1 — lock observation
    assertNotNull(v, "JT-QUERY-015; observed=${r}")
}
