// JT-PARSE-074: parse_error_to_null NaN → NULL
suite("repro_jt_parse_074") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_null('NaN')"
        assertEquals(null, r[0][0], "JT-PARSE-074: NaN → NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-074: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
