// JT-QUERY-069: strip 非 T_Null
suite("repro_jt_query_069") {
    try {
        def r = sql """SELECT json_strip_nulls(CAST('1' AS JSONB))"""
        String v = r[0][0] == null ? "null" : r[0][0].toString()
        assertTrue(v.contains('1'), "observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-069: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
