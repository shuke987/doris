// JT-PARSE-068: jsonb_parse_error_to_null NULL 输入
suite("repro_jt_parse_068") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_null(NULL)"
        assertEquals(null, r[0][0],
            "JT-PARSE-068: NULL → NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-068: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
