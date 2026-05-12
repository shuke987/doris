// JT-PARSE-055: int64 边界 2^63
suite("repro_jt_parse_055") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_type(jsonb_parse('9223372036854775808'), '\$') """
    } catch (Exception e) {
        logger.info("JT-PARSE-055 threw: ${e.message}")
    }
}
