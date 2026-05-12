// JT-PARSE-057: decimal 大数
suite("repro_jt_parse_057") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('1234567890.1234567890123') """
    } catch (Exception e) {
        logger.info("JT-PARSE-057 threw: ${e.message}")
    }
}
