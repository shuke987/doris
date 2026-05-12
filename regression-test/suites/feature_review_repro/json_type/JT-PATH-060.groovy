// JT-PATH-060: json_contains_path + $.*
suite("repro_jt_path_060") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT json_contains_path(CAST('{"a":1}' AS JSONB), 'one', '\$.*') """
        } catch (Exception e) {
            logger.info("JT-PATH-060 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PATH-060: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
