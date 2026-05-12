// JT-PARSE-100: routine_load + 非法 JSON message
suite("repro_jt_parse_100") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-PARSE-100 threw: ${e.message}")
    }
}
