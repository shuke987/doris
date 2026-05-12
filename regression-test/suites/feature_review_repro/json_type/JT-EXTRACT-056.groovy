// JT-EXTRACT-056: extract_int 对 jsonb null
suite("repro_jt_extract_056") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":null}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0], "JT-EXTRACT-056; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-056: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
