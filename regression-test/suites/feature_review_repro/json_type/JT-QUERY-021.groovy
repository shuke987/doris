// JT-QUERY-021: json_keys array (non-object) → NULL
suite("repro_jt_query_021") {
    def r = sql "SELECT json_keys(CAST('[1,2]' AS JSONB))"
    assertEquals(null, r[0][0], "JT-QUERY-021; observed=${r}")
}
