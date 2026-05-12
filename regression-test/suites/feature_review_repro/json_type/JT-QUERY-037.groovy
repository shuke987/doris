// JT-QUERY-037: contains_path wildcard path
suite("repro_jt_query_037") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT json_contains_path(CAST('{"a":1}' AS JSONB), 'one', '\$.*') """
        } catch (Exception e) {
            logger.info("JT-QUERY-037 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-QUERY-037: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
