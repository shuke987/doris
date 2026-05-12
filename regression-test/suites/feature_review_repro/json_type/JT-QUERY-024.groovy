// JT-QUERY-024: contains NULL jsonb
suite("repro_jt_query_024") {
    def r = sql """SELECT json_contains(CAST(NULL AS JSONB), CAST('1' AS JSONB))"""
    assertEquals(null, r[0][0], "JT-QUERY-024: expect NULL; observed=${r}")
}
