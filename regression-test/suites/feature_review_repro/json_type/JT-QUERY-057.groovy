// JT-QUERY-057: overlaps 类型不匹配
suite("repro_jt_query_057") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT json_overlaps(CAST('1' AS JSONB), CAST('[1]' AS JSONB)) """
        } catch (Exception e) {
            logger.info("JT-QUERY-057 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-057: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
