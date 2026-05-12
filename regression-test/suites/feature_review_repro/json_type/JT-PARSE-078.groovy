// JT-PARSE-078: jsonb_parse_error_to_value 1 参 非法 → {}
suite("repro_jt_parse_078") {
    try {
        def r = sql "SELECT jsonb_parse_error_to_value('{a:1')"
        assertEquals("{}", r[0][0].toString(),
            "JT-PARSE-078: 1-arg illegal → default '{}'; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-078: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
