// JT-PARSE-085: NULL input + default → NULL (FOLLOW_INPUT)
suite("repro_jt_parse_085") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_value(NULL, '{}')"
        assertEquals(null, r[0][0],
            "JT-PARSE-085: NULL input → NULL (not default, FOLLOW_INPUT); observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-085: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
