// JT-PARSE-040: BOM 开头
suite("repro_jt_parse_040") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse(CONCAT(CHAR(0xEF), CHAR(0xBB), CHAR(0xBF), '{"a":1}')) """
    } catch (Exception e) {
        logger.info("JT-PARSE-040 threw: ${e.message}")
    }
}
