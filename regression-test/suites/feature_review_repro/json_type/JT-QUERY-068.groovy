// JT-QUERY-068: strip top-level T_Null
suite("repro_jt_query_068") {
    try {
        def r = sql """SELECT json_strip_nulls(CAST('null' AS JSONB))"""
        assertEquals(null, r[0][0], "JT-QUERY-068: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-068: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
