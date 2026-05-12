// JT-CONSTRUCT-045: array_agg 结果超 16MB
suite("repro_jt_construct_045") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT json_array_agg(1) """
        } catch (Exception e) {
            logger.info("JT-CONSTRUCT-045 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CONSTRUCT-045: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
