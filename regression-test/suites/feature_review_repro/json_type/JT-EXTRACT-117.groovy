// JT-EXTRACT-117: depth nested
suite("repro_jt_extract_117") {
    try {
        def r = sql """SELECT json_depth(CAST('{"a":{"b":1}}' AS JSONB))"""
        assertEquals('3', r[0][0]?.toString(), "JT-EXTRACT-117; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-117: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
