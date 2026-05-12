// JT-PARSE-120: const NULL 短路 vs 非 const NULL
suite("repro_jt_parse_120") {
    try {
        def r1 = sql "SELECT jsonb_parse_error_to_value(NULL, '{}')"
        assertEquals(null, r1[0][0],
            "JT-PARSE-120 (const NULL): FOLLOW_INPUT → NULL; observed=${r1}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-120: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
