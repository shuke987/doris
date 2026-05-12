// JT-PARSE-039: Unicode lone surrogate
suite("repro_jt_parse_039") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":"\uD800"}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-039 threw: ${e.message}")
    }
}
