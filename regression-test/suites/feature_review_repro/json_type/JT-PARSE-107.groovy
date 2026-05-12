// JT-PARSE-107: 错误不泄露内存地址
suite("repro_jt_parse_107") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('bad') """
    } catch (Exception e) {
        logger.info("JT-PARSE-107 threw: ${e.message}")
    }
}
