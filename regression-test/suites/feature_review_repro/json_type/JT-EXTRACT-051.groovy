// JT-EXTRACT-051: extract_int 对 string "abc" — 应 NULL
suite("repro_jt_extract_051") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":\"abc\"}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0],
            "JT-EXTRACT-051: extract_int on non-numeric string → NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-051: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
