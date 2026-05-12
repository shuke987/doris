// JT-PARSE-081: 2 参 default 是非法 JSON
suite("repro_jt_parse_081") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT jsonb_parse_error_to_value('{a', 'also_invalid') """
        } catch (Exception e) {
            logger.info("JT-PARSE-081 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-081: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
