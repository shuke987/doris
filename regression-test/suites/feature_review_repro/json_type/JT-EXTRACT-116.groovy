// JT-EXTRACT-116: depth empty array
suite("repro_jt_extract_116") {
    try {
        def r = sql """SELECT json_depth(CAST('[]' AS JSONB))"""
        assertEquals('1', r[0][0]?.toString(), "JT-EXTRACT-116; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-116: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
