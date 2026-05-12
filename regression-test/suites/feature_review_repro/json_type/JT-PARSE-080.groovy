// JT-PARSE-080: jsonb_parse_error_to_value 2 参 非法 → default '[1]'
suite("repro_jt_parse_080") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_value('{a', '[1]')"
        assertEquals("[1]", r[0][0].toString(),
            "JT-PARSE-080: 2-arg illegal → user default; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-080: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
