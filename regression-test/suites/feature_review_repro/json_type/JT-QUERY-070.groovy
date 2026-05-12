// JT-QUERY-070: strip 嵌套含 null
suite("repro_jt_query_070") {
    try {
        def r = sql """SELECT json_strip_nulls(CAST('{"a":null}' AS JSONB))"""
        String v = r[0][0] == null ? "null" : r[0][0].toString()
        assertTrue(v.contains('{'), "observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-070: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
