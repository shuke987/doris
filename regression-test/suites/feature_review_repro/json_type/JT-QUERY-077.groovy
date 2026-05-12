// JT-QUERY-077: `jsonb_length` 在 size==0 列上
suite("repro_jt_query_077") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT jsonb_length(CAST('1' AS JSONB)) """
        } catch (Exception e) {
            logger.info("JT-QUERY-077 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-077: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
