// JT-QUERY-036: contains_path NULL jsonb
suite("repro_jt_query_036") {
    try {
        def r = sql """SELECT json_contains_path(CAST(NULL AS JSONB), 'one', '\$.a')"""
        assertEquals(null, r[0][0], "JT-QUERY-036: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-036: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
