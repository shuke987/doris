// JT-EXTRACT-055: extract_int 对 object
suite("repro_jt_extract_055") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":{\"x\":1}}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0], "JT-EXTRACT-055; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-055: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
