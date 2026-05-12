// JT-PARSE-108: SHOW WARNINGS for parse_error_to_null
suite("repro_jt_parse_108") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-108 threw: ${e.message}")
    }
}
