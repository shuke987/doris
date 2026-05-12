// JT-PARSE-075: 二进制 corrupt input
suite("repro_jt_parse_075") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse(CONCAT('{', CHAR(0), '}')) """
    } catch (Exception e) {
        logger.info("JT-PARSE-075 threw: ${e.message}")
    }
}
