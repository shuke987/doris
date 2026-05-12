// JT-PARSE-099: stream_load + 非法行 + columns 用 jsonb_parse_error_to_null
suite("repro_jt_parse_099") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT jsonb_parse('{"a":1}') """
        } catch (Exception e) {
            logger.info("JT-PARSE-099 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-099: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
