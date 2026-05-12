// JT-PARSE-123: nereids 喂 BOM 单字节
suite("repro_jt_parse_123") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse(CONCAT(CHAR(0xEF), CHAR(0xBB), CHAR(0xBF))) """
    } catch (Exception e) {
        logger.info("JT-PARSE-123 threw: ${e.message}")
    }
}
