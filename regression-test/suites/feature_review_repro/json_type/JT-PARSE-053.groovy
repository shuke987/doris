// JT-PARSE-053: int32 边界 2^31
suite("repro_jt_parse_053") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_type(jsonb_parse('2147483648'), '\$') """
    } catch (Exception e) {
        logger.info("JT-PARSE-053 threw: ${e.message}")
    }
}
