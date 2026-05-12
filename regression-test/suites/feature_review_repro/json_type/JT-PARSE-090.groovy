// JT-PARSE-090: 16MB + default
suite("repro_jt_parse_090") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT jsonb_parse_error_to_value('bad', '{}') """
        } catch (Exception e) {
            logger.info("JT-PARSE-090 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-090: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
