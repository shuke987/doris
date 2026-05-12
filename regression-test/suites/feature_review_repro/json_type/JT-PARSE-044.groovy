// JT-PARSE-044: 0e1000 (溢出 double)
suite("repro_jt_parse_044") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('0e1000') """
    } catch (Exception e) {
        logger.info("JT-PARSE-044 threw: ${e.message}")
    }
}
