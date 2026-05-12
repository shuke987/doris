// JT-PARSE-046: 极大整数 2^127
suite("repro_jt_parse_046") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('170141183460469231731687303715884105728') """
    } catch (Exception e) {
        logger.info("JT-PARSE-046 threw: ${e.message}")
    }
}
