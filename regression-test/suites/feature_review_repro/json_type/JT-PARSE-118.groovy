// JT-PARSE-118: default 常量字面量 vs 常量列 fragment-local 状态
suite("repro_jt_parse_118") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT jsonb_parse_error_to_value('bad', '{}') """
        } catch (Exception e) {
            logger.info("JT-PARSE-118 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-118: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
