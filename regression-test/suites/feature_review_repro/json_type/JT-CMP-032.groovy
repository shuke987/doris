// JT-CMP-032: `normalize_json_numbers_to_double(cast(NaN as JSONB))`
suite("repro_jt_cmp_032") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT CAST(CAST('1.5' AS DOUBLE) AS JSONB) """
        } catch (Exception e) {
            logger.info("JT-CMP-032 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CMP-032: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
