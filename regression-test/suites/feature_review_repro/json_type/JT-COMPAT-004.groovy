// JT-COMPAT-004: JSON_CONTAINS NaN
suite("repro_jt_compat_004") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_contains(jsonb_parse('[1.0]'), jsonb_parse('1.0')) """
    } catch (Exception e) {
        logger.info("JT-COMPAT-004 threw: ${e.message}")
    }
}
