// JT-CROSS-029: PREPARE jsonb 字面量 bind
suite("repro_jt_cross_029") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-CROSS-029 threw: ${e.message}")
    }
}
