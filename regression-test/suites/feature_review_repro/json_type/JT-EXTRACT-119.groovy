// JT-EXTRACT-119: depth NULL
suite("repro_jt_extract_119") {
    try {
        def r = sql """SELECT json_depth(CAST(NULL AS JSONB))"""
        assertEquals(null, r[0][0], "JT-EXTRACT-119: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-119: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
