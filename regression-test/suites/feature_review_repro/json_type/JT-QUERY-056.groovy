// JT-QUERY-056: overlaps NULL
suite("repro_jt_query_056") {
    try {
        def r = sql """SELECT json_overlaps(CAST(NULL AS JSONB), CAST('[1]' AS JSONB))"""
        assertEquals(null, r[0][0], "JT-QUERY-056: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-056: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
