// JT-EXTRACT-054: extract_int int128 over → NULL
suite("repro_jt_extract_054") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":12345678901234567890}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0],
            "JT-EXTRACT-054: int overflow → NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-054: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
