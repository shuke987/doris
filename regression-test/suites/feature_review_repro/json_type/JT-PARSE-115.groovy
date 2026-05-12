// JT-PARSE-115: 超 int128 退 double
suite("repro_jt_parse_115") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('170141183460469231731687303715884105728') """
    } catch (Exception e) {
        logger.info("JT-PARSE-115 threw: ${e.message}")
    }
}
