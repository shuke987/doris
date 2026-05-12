// JT-QUERY-034: contains_path NULL mode
suite("repro_jt_query_034") {
    try {
        def r = sql """SELECT json_contains_path(CAST('{"a":1}' AS JSONB), NULL, '\$.a')"""
        assertEquals(null, r[0][0], "JT-QUERY-034: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-034: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
