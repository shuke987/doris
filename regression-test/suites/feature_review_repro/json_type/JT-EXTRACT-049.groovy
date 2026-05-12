// JT-EXTRACT-049: extract_int 正向
suite("repro_jt_extract_049") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":1}' AS JSONB), '\$.a')"
        assertEquals("1", r[0][0].toString(), "JT-EXTRACT-049; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-049: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
