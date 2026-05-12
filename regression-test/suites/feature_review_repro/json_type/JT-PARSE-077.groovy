// JT-PARSE-077: jsonb_parse_error_to_value 1 参 合法
suite("repro_jt_parse_077") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_value('{\"a\":1}')"
        assertEquals("{\"a\":1}", r[0][0].toString(),
            "JT-PARSE-077: 1-arg legal; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-077: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
