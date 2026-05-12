// JT-QUERY-009: length NULL jsonb
suite("repro_jt_query_009") {
    def r = sql """SELECT json_length(CAST(NULL AS JSONB))"""
    assertEquals(null, r[0][0], "JT-QUERY-009: expect NULL; observed=${r}")
}
