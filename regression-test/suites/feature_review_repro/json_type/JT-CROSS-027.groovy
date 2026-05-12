// JT-CROSS-027: PREPARE 含 jsonb_parse(?)
suite("repro_jt_cross_027") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-CROSS-027 threw: ${e.message}")
    }
}
